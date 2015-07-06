package be.ehb.medicationreminder;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;


/**
 * Created by davy.van.belle on 30/06/2015.
 */
public class MedicationReminder extends Application{


    private MedicationMap medMap = null;
    private String TAG = "MED_REM_APPLICATION";

    @Override
    public void onCreate() {
        super.onCreate();
        loadDB();
    }

    public void setMedicationMap(MedicationMap medicationMap){
        medMap = medicationMap;
    }

    public MedicationMap getMedicationMap (){
        if (medMap == null){
            loadDB();
        }
            return medMap;
    }

    private void loadDB(){
        medMap = MedicationMap.getInstance();
        MedicationDAO medicationDAO = new MedicationDAO(this);
        AlarmDAO alarmDAO = new AlarmDAO(this);
        Log.d(TAG,"Loading Database");
        medMap.putAll(medicationDAO.getAll());

        for (Map.Entry<Integer,Medication> entry : medMap.entrySet())
        {
            Medication tMed = entry.getValue();
            tMed.addAlarms(alarmDAO.getAlarmsByMed(tMed));
        }
    }
}
