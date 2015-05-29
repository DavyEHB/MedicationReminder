package be.ehb.medicationreminder.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.Alarms;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationList;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;

public class MedicationAddActivity extends Activity implements MedicationAddFragment.Callback{

    public static final int ADD_NEW_MED = 1;

    Medication mItem = new Medication("");

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

    /*
    @Override
    public void onAddButtonClicked() {
        if (mItem.getName() != ""){
            MedicationDAO medDAO = new MedicationDAO(this);
            AlarmDAO alarmDAO = new AlarmDAO(this);

            ArrayList<Alarms> tmpAlarms = (ArrayList<Alarms>) mItem.getAlarms().clone();
            mItem.getAlarms().clear();
            mItem = medDAO.insert(mItem);
            mItem.addAlarms(tmpAlarms);
            Iterator<Alarms> alIT = tmpAlarms.iterator();

            while (alIT.hasNext())
            {
                Alarms al = alIT.next();
                Log.d("ALARMS",al.toString());
                alarmDAO.insert(al);
            }

            MedicationList.getInstance().addMedication(mItem);
            Log.d("MEDID",String.valueOf(mItem.toString()));
            this.finish();
        }
        else
        {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Medication name is empty", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onCancelClicked() {
        mItem = null;
        this.finish();
    }

    @Override
    public void onNameClicked() {
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        new AlertDialog.Builder(this)
                .setTitle("Medication name")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mItem.setName(input.getText().toString());
                        ((TextView) findViewById(R.id.new_med_name)).setText(mItem.getName());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }
    */
}
