package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import be.ehb.medicationreminder.Connection.WearConnector;
import be.ehb.medicationreminder.MedicationReminder;
import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.MedicationDAO;


public class StartActivity extends Activity{

    private String TAG = "START_ACTIVITY";
    private MedicationReminder application;
    private MedicationMap medicationMap;
    private WearConnector wearConnector;
    private MedicationDAO medicationDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        application = ((MedicationReminder)getApplicationContext());

        wearConnector = new WearConnector(this);
        wearConnector.connect();
        medicationDAO = new MedicationDAO(this);
    }

    @Override
    protected void onStop() {
        wearConnector.disconnect();
        super.onStop();
    }


    public void onConnectClick(View view){
        Log.d(TAG,"onConnectClicked");
        Medication newMed = new Medication("CRUD");
        newMed.setID(25);
        wearConnector.pushToWear(application.getMedicationMap());
        //wearConnector.startApp();

        //wearConnector.getNextID();


        /*
        medicationMap = application.getMedicationMap();
        Log.d(TAG,"Application var: " + medicationMap.get(1).getName());
        */
        /*
        Intent intent = new Intent(this,MedicationListActivity.class);
        startActivity(intent);
        */
    }

    public void onTestClick(View view){
        Log.d(TAG,"onConnectClicked");
        //wearConnector.startApp();
    }
}
