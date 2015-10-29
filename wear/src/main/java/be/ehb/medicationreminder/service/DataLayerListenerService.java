package be.ehb.medicationreminder.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import be.ehb.medicationreminder.statics.MedicationStatics;
import be.ehb.medicationreminder.UI.MainActivity;
import be.ehb.medicationreminder.core.Alarm;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;

public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "DataLayerListener";

    GoogleApiClient mGoogleApiClient;

    private MedicationMap medicationMap = MedicationMap.getInstance();
    private MedicationDAO medicationDAO = null;
    private AlarmDAO alarmDAO = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        medicationDAO = new MedicationDAO(this);
        alarmDAO = new AlarmDAO(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Service destroyed");
        super.onDestroy();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.d(TAG,"onDataChanged: ");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (MedicationStatics.MED_PUSH_PATH.equals(path)) {
                    //insert into database
                    insertMedIntoDB(event);
                } else {
                        Log.d(TAG, "Unrecognized path: " + path);

                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d(TAG, "Type deleted");

            } else {
                Log.d(TAG, "unknown type");
            }
        }
    }

    private void insertMedIntoDB(DataEvent event){
        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
        int ID = dataMapItem.getDataMap().getInt(MedicationStatics.MED_ID);
        String name = dataMapItem.getDataMap().getString(MedicationStatics.MED_NAME);
        Medication medication = new Medication(ID,name);
        Asset picAsset = dataMapItem.getDataMap().getAsset(MedicationStatics.MED_PIC);
        if (picAsset != null) {
            Bitmap pic = loadBitmapFromAsset(mGoogleApiClient, picAsset);
            medication.setPicture(pic);
        }

        medication = medicationDAO.insert(medication);
        medicationMap.put(medication);
        Log.d(TAG, "Name: " + medicationMap.get(medication.getID()) + "ID: " + medication.getID());

        ArrayList<DataMap> alarms = dataMapItem.getDataMap().getDataMapArrayList(MedicationStatics.MED_ALARM_LIST);

        for (DataMap alarmMap : alarms){
            int alarmID = alarmMap.getInt(MedicationStatics.ALARM_ID);
            String time = alarmMap.getString(MedicationStatics.ALARM_TIME);
            String days = alarmMap.getString(MedicationStatics.ALARM_DAYS);

            Alarm alarm = new Alarm(alarmID,time,days,ID);
            alarmDAO.insert(alarm);
            Log.d(TAG,"New Alarm: " + alarm.toString());
            medication.addAlarm(alarm);
        }
        Log.d(TAG,"Database:");
        Log.d(TAG,medicationMap.toString());

        Intent intent = new Intent(this, StartService.class);
        startService(intent);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);
        String path = messageEvent.getPath();
        if (path.equals(MedicationStatics.MSG_PATH)) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, "Message: " + message);
        } else if (path.equals(MedicationStatics.START_APP)){
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (path.equals(MedicationStatics.DELETE_ALL)){
            medicationDAO.deleteAll();
            alarmDAO.deleteAll();
            medicationMap.clear();
        }
    }

    private Bitmap loadBitmapFromAsset(GoogleApiClient apiClient, Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }

        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                apiClient, asset).await().getInputStream();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        return BitmapFactory.decodeStream(assetInputStream);
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);
    }

    @Override
    public void onConnectedNodes(List<Node> connectedNodes) {
        super.onConnectedNodes(connectedNodes);
    }

    protected void sendMessage(String path, String message) {
        Log.d(TAG,"Sending...");
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, "TEST", path,message.getBytes()).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to sendResult message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                        else {
                            Log.d(TAG,"Success sending message");
                        }
                    }
                }
        );
    }


}
