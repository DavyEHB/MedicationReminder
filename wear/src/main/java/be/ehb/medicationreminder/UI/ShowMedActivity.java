package be.ehb.medicationreminder.UI;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.MedicationDAO;
import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.service.StartService;

public class ShowMedActivity extends Activity {

    private static final String TAG = "SHOW_MED_ACTIVITY";

    public static final String EXTRA_MED_ID = "be.ehb.medicationreminder.extra_med_id";

    private TextView mTextView;

    private TextView text;
    private ImageView imageView;

    private MedicationMap medList = MedicationMap.getInstance();

    private Medication medication;

    private MedicationDAO medicationDAO = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        Log.d(TAG,"ShowMed created at: " + now.getTime());
        setContentView(R.layout.activity_show_med);

        int id = getIntent().getIntExtra(EXTRA_MED_ID,0);
        medicationDAO = new MedicationDAO(this);
        medication = medicationDAO.getByID(id);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupLayout(stub);
            }
        });

        Intent intent = new Intent(this, StartService.class);
        startService(intent);
    }


    protected void setupLayout(WatchViewStub stub){
        text = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.imageView);

        text.setText(medication.getName());
        imageView.setBackgroundColor(Color.rgb(0, 0, 0));
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.no_image));

        if (medication.getPicture() != null) {
            imageView.setImageBitmap(medication.getPicture());
        }

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vib.hasVibrator())
        {
            vib.vibrate(2000);
        }
    }
}
