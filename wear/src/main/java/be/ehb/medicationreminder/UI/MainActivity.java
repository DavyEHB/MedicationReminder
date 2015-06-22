package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.WatchViewStub;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import be.ehb.medicationreminder.core.Alarms;
import be.ehb.medicationreminder.core.DayOfWeek;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;
import be.ehb.medicationsreminder.R;


public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 0;
    private static final String TAG = "MAIN_ACTIVITY";

    private MedicationDAO medicationDAO = null;
    private MedicationMap medicationMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WEAR", "created");
        setContentView(R.layout.activity_main);

        loadMedications(this);
/*
        BoxInsetLayout boxLayout = (BoxInsetLayout) findViewById(R.id.box_inset_layout);

        boxLayout.setBackground(getResources().getDrawable(R.drawable.dummy_image));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create("Take Meds", "Dafalgan Codeine");
        cardFragment.setCardGravity(Gravity.BOTTOM);
        //fragmentTransaction.add(R.id.frame_layout, cardFragment);
        //fragmentTransaction.commit();

        */


    }

    public void onClickMe(View view) {


/*
        Intent intent = new Intent(this, ShowMedActivity.class);
        intent.putExtra(ShowMedActivity.EXTRA_MED_ID,medication.getID());
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //startActivity(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.setTimeInMillis(System.currentTimeMillis());
        triggerTime.add(Calendar.MINUTE, 1);
        triggerTime.set(Calendar.SECOND, 0);

        //Time now
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), pendingIntent);
        Log.d(TAG, "Current time: " + now.getTime());
        Log.d(TAG,"Set time: "+ triggerTime.getTime());
*/
        //setAlarms();
        test2();

//        this.finish();
    }

    private void setAlarms()
    {
        //Time now
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        Log.d(TAG,"Current time: " + now.getTime());
        for (Map.Entry<Integer,Medication> entry : medicationMap.entrySet())
        {
            Medication tMed = entry.getValue();
            Log.d(TAG,"Setting alarms for: " + tMed.getName());

            for (Alarms alarm : tMed.getAlarms())
            {
                Intent intent = new Intent(this, ShowMedActivity.class);
                intent.putExtra(ShowMedActivity.EXTRA_MED_ID,tMed.getID());
                intent.setAction(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                //startActivity(intent);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,0);

                AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
                Calendar triggerTime = Calendar.getInstance();
                triggerTime.setTimeInMillis(System.currentTimeMillis());
                triggerTime.add(Calendar.MINUTE, 1);
                triggerTime.set(Calendar.SECOND, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), pendingIntent);
                Log.d(TAG,"Alarm set: "+ triggerTime.getTime());
            }
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
        now.set(Calendar.MINUTE,53);

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

        tTime.set(Calendar.HOUR,7);
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
