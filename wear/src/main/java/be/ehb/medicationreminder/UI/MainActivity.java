package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import be.ehb.medicationreminder.core.DayOfWeek;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;
import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.service.StartService;


public class MainActivity extends Activity{

    private static final int REQUEST_CODE = 0;
    private static final String TAG = "MAIN_ACTIVITY";

    private MedicationDAO medicationDAO = null;
    private MedicationMap medicationMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WEAR", "created");
        setContentView(R.layout.activity_main);
    }

    public void onClickYes(View view) {

        Intent intent = new Intent(this, StartService.class);
        startService(intent);
        this.finish();
    }

    public void onClickNo(View view)
    {
        this.finish();
    }

    private void setAlarms()
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

    private void loadMedications(Activity activity)
    {
        medicationMap = MedicationMap.getInstance();
        medicationDAO = new MedicationDAO(activity);
        AlarmDAO alarmDAO = new AlarmDAO(activity);
        Log.d(TAG,"Loading Database");
        medicationMap.putAll(medicationDAO.getAll());

        for (Map.Entry<Integer,Medication> entry : medicationMap.entrySet())
        {
            Medication tMed = entry.getValue();
            tMed.addAlarms(alarmDAO.getAlarmsByMed(tMed));
        }
    }

    private void test2()
    {
        Calendar now = Calendar.getInstance();

        Medication medication = medicationMap.getNextMedication(now);
        Log.d(TAG,"Next medication: " + medication.getName());
    }

    private void test(int day)
    {

        ArrayList<DayOfWeek> DOW = new ArrayList<>();
        DOW.add(DayOfWeek.MON);
        DOW.add(DayOfWeek.WED);
        DOW.add(DayOfWeek.SAT);
        int nextDay = 8;

        //Time now
        Calendar now = Calendar.getInstance();

        //Set Test Time
        now.setTimeInMillis(System.currentTimeMillis());
        now.set(Calendar.DAY_OF_WEEK,day);

        Calendar tTime = (Calendar) now.clone();

        //Covert to Monday = 1 Sunday = 7
        int day_of_week = now.get(Calendar.DAY_OF_WEEK);
        --day_of_week;
        if (day_of_week == 0) day_of_week = 7;

        tTime.set(Calendar.HOUR_OF_DAY,7);
        tTime.set(Calendar.MINUTE,30);
        tTime.set(Calendar.SECOND,0);

        for (DayOfWeek dow : DOW)
        {
            int diffDay = dow.ordinal() + 1 - day_of_week;
            if(diffDay < 0){
                diffDay = diffDay + 7;
            }

            if (nextDay > diffDay) {
                nextDay = diffDay;
            }
        }

        if (tTime.before(now))
        {
            ++day_of_week;
            nextDay = 8;
            for (DayOfWeek dow : DOW)
            {
                int diffDay = dow.ordinal() + 1 - day_of_week;
                if(diffDay < 0){
                    diffDay = diffDay + 7;
                }

                if (nextDay > diffDay) {
                    nextDay = diffDay;
                }
            }
            ++nextDay;
        }

        tTime.add(Calendar.DAY_OF_WEEK,nextDay);

        Log.d(TAG,"Current time: " + now.getTime() + " - Next time: " + tTime.getTime());
    }
}
