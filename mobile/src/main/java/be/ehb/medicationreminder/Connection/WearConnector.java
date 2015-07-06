package be.ehb.medicationreminder.Connection;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import be.ehb.medicationreminder.statics.MedicationStatics;
import be.ehb.medicationreminder.core.Alarm;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;

/**
 * Created by davy.van.belle on 26/06/2015.
 */
public class WearConnector implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "ABSTRACT_CONN";
    private boolean mResolvingError = false;
    private GoogleApiClient mGoogleApiClient;

    private Context mContext;

    public WearConnector(Context context){
        Log.d(TAG,"Creating CRUD");
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"Connection succeed");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"Connection failed");
    }

    public void pushToWear(MedicationMap medicationMap){
        sendMessage(MedicationStatics.DELETE_ALL,"");
        Log.d(TAG,medicationMap.toString());
        for (Medication med : medicationMap.values()){
            Log.d(TAG,"current Med: " + med.toString());
            pushMed(med);
        }
    }

    private void pushMed(Medication medication){
        PutDataMapRequest dataMap = PutDataMapRequest.create(MedicationStatics.MED_PUSH_PATH);
        DataMap map = dataMap.getDataMap();
        map.putLong("time", new Date().getTime());
        map.putInt(MedicationStatics.MED_ID, medication.getID());
        map.putString(MedicationStatics.MED_NAME, medication.getName());

        //Alarms

        ArrayList<DataMap> alarms = new ArrayList<>();
        for (Alarm alarm : medication.getAlarms()) {
            DataMap alarmMap = new DataMap();
            alarmMap.putInt(MedicationStatics.ALARM_ID, alarm.getID());
            alarmMap.putString(MedicationStatics.ALARM_TIME, alarm.getTime());
            alarmMap.putString(MedicationStatics.ALARM_DAYS, alarm.printDays());
            alarmMap.putInt(MedicationStatics.ALARM_PARENT, alarm.getParentID());
            alarms.add(alarmMap);
        }

        map.putDataMapArrayList(MedicationStatics.MED_ALARM_LIST,alarms);

        if (medication.getPicture() != null) {
            map.putAsset(MedicationStatics.MED_PIC, toAsset(medication.getPicture()));
        }
        sendDataMap(dataMap);
    }

    private static Asset toAsset(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        } finally {
            if (null != byteStream) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
        
    protected void sendMessage(String path, String message) {
        Log.d(TAG,"Sending...");
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, "TEST", path,message.getBytes()).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                        else {

                            Log.d(TAG,"Success sending message");
                        }
                    }
                }
        );
    }

    protected void sendDataMap(PutDataMapRequest dataMap) {
        Log.d(TAG,"Sending data ...");
        PutDataRequest request = dataMap.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        Log.d(TAG, "Sending image was successful: " + dataItemResult.getStatus()
                                .isSuccess());
                    }
                });
    }
}
