package be.ehb.medicationreminder.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;

import be.ehb.medicationreminder.R;

public class MedicationAddActivity extends Activity implements MedicationAddFragment.OnFragmentInteractionListener{

    public static final int ADD_NEW_MED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_add);
        if (savedInstanceState == null) {
            MedicationAddFragment fragment = new MedicationAddFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.medication_add_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onButtonClicked() {
        this.finish();
    }
}
