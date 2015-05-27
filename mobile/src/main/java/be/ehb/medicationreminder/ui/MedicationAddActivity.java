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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_medication_add, container, false);
            return rootView;
        }
    }
}
