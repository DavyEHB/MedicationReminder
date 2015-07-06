package be.ehb.medicationreminder.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Map;

import be.ehb.medicationreminder.UI.ShowMedActivity;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;

/**
 * Created by davy.van.belle on 23/06/2015.
 */
public class StartService extends IntentService
{
    private static final String TAG = "START_SERVICE";
    private static final int REQUEST_CODE = 12;

    private MedicationDAO medicationDAO = null;
    private static MedicationMap medicationMap = MedicationMap.getInstance();


    public StartService() {
        super("StartService");
    }

    private void setNextAlarm()
    {
        Calendar now = Calendar.getInstance();

        Medication nextMed = medicationMap.getNextMedication(now);

        if (nextMed != null) {
            Intent intent = new Intent(this, ShowMedActivity.class);
            intent.putExtra(ShowMedActivity.EXTRA_MED_ID, nextMed.getID());
            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, 0);

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            Calendar triggerTime = nextMed.getNextAlarm(now);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), pendingIntent);
            Log.d(TAG, "Current time: " + now.getTime());
            Log.d(TAG, "Set time: " + triggerTime.getTime());
        }
        else
        {
            Log.d(TAG,"No alarm to set");
        }
    }

    private void loadMedications(Context context)
    {
        medicationMap = MedicationMap.getInstance();
        medicationDAO = new MedicationDAO(context);
        AlarmDAO alarmDAO = new AlarmDAO(context);
        Log.d(TAG,"Loading Database");
        medicationMap.putAll(medicationDAO.getAll());

        for (Map.Entry<Integer,Medication> entry : medicationMap.entrySet())
        {
            Medication tMed = entry.getValue();
            tMed.addAlarms(alarmDAO.getAlarmsByMed(tMed));
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"Start Service onCreate");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"Start Service onHandleIntent");
        loadMedications(getApplicationContext());
        setNextAlarm();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Start Service destroyed");
        super.onDestroy();
    }
}
