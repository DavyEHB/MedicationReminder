package be.ehb.medicationreminder.UI;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import be.ehb.medicationreminder.R;

/**
 * An activity representing a list of Medications. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MedicationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MedicationListFragment} and the item details
 * (if present) is a {@link MedicationDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MedicationListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MedicationListActivity extends Activity
        implements MedicationListFragment.Callbacks,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LIST_ACTIVITY";
    public static final String MED_MAP = "be.ehb.medrem/med_map";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private GoogleApiClient mGoogleApiClient;
    private String MSG_PATH = "/msg";

    @Override
    protected void onStart() {
        super.onStart();
        boolean mResolvingError = true;
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        if (findViewById(R.id.medication_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            MedicationListFragment listFrag;
            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            listFrag = ((MedicationListFragment) getFragmentManager()
                    .findFragmentById(R.id.medication_list));
            listFrag.setHasOptionsMenu(true);

        }


    }

    /**
     * Callback method from {@link MedicationListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(MedicationDetailFragment.ARG_ITEM_ID, id);
            MedicationDetailFragment fragment = new MedicationDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.medication_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MedicationDetailActivity.class);
            detailIntent.putExtra(MedicationDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
/*
    @Override
    public void onAddMedicationFragment() {
        Log.d("NEW MED","Starting new activity");
        Intent addIntent = new Intent(this, MedicationAddActivity.class);
        startActivityForResult(addIntent, MedicationAddActivity.ADD_NEW_MED);
    }
    */

    public void onAddMedClick(MenuItem item){
        Log.d("NEW MED","Starting new activity");
        Intent addIntent = new Intent(this, MedicationAddActivity.class);
        startActivityForResult(addIntent, MedicationAddActivity.ADD_NEW_MED);
    }

    public void onSyncClick(MenuItem item){
        Log.d(TAG, "onSyncClick");
        /*
        PutDataMapRequest dataMap = PutDataMapRequest.create(IMAGE_PATH);
        dataMap.getDataMap().putLong("time", new Date().getTime());
        PutDataRequest request = dataMap.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        Log.d(TAG, "Sending image was successful: " + dataItemResult.getStatus()
                                .isSuccess());
                    }
                });
                */
        sendMessage("TEST");
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


    private void sendMessage(String msg) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, "TEST", MSG_PATH,msg.getBytes()).setResultCallback(
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
}
