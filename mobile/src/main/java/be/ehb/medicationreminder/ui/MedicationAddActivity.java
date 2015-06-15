package be.ehb.medicationreminder.ui;

import android.app.Activity;
import android.os.Bundle;

import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.R;


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
