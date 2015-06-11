package be.ehb.medicationreminder.wear.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationList;


public class MainActivity extends Activity {

    private TextView mTextView;

    MedicationList medicationList = MedicationList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

                Medication mItem = medicationList.getMedicationList().get(0);

                //mTextView.setText(mItem.getName());

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
