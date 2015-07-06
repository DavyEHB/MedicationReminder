package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.R;


public class VibrateActivity extends Activity {

    private TextView mTextView;


    MedicationMap medicationMap = MedicationMap.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);

        Medication med1 = new Medication("Testtt");
        //medicationList.addMedication(med1);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

               // Medication mItem = medicationList.getMedicationList().get(0);

               // mTextView.setText(mItem.getName());

                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vib.hasVibrator())
                {
                    vib.vibrate(500);
                }
            }
        });

        stub.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                // Need to also call the original insets since we have overridden the original
                // https://developer.android.com/reference/android/view/View.OnApplyWindowInsetsListener.html
                stub.onApplyWindowInsets(windowInsets);

                // this is where you would check the WindowInsets for isRound()

                return windowInsets;
            }
        });
    }
}
