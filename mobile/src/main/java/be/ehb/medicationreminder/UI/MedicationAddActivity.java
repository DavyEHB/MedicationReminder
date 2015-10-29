package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import be.ehb.medicationreminder.MedicationReminder;
import be.ehb.medicationreminder.core.Alarm;
import be.ehb.medicationreminder.core.DayOfWeek;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;


public class MedicationAddActivity extends Activity implements MedicationAddFragment.Callback,
        TimePickerDialog.Callback {

    public static final int ADD_NEW_MED = 1;

    private final int SELECT_PHOTO = 1;
    private final int SELECT_TIME = 2;

    private static final String TAG = "MEDICATION_ADD_ACTIVITY";

    private MedicationReminder application;
    private Medication mItem = null;
    private MedicationMap medMap = null;

    private ArrayAdapter adapter = null;

    private final Context context = this;

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
        application = (MedicationReminder) getApplication();
        medMap = application.getMedicationMap();
        mItem = new Medication("");
    }


    @Override
    public boolean onAddImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        return false;
    }

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
            mItem = medDAO.pushObjectToDB(mItem);
            mItem.addAlarms(tmpAlarms);
            Iterator<Alarms> alIT = tmpAlarms.iterator();

            while (alIT.hasNext())
            {
                Alarms al = alIT.next();
                Log.d("ALARMS",al.toString());
                alarmDAO.pushObjectToDB(al);
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

    public void onAddClick(View view) {
        if (!mItem.getName().equals("")) {
            MedicationDAO medDAO = new MedicationDAO(this);
            AlarmDAO alarmDAO = new AlarmDAO(this);
            ArrayList<Alarm> tmpAlarms = (ArrayList<Alarm>) mItem.getAlarms().clone();
            mItem.getAlarms().clear();
            mItem = medDAO.insert(mItem);
            mItem.addAlarms(tmpAlarms);

            for(Alarm alarm : tmpAlarms){
                Log.d(TAG,"Add alarm: " + alarm.toString());
                alarmDAO.insert(alarm);
            }

            medMap.put(mItem);
            Log.d(TAG,"Add medication: " + String.valueOf(mItem.toString()));
            this.finish();
        } else {
            Toast toast = Toast.makeText(this, "Medication name is empty", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onCancelClick(View view) {
        mItem = null;
        this.finish();
    }

    public void onNewNameClick(View view) {
        Log.d(TAG,"NexNameClicked");
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

    public void addAlarm(){
        Log.d("NEW", "NEW ALARM FOR " + mItem.getName());
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        ArrayList<DayOfWeek> everyDay = new ArrayList<DayOfWeek>();
        everyDay.add(DayOfWeek.MON);
        everyDay.add(DayOfWeek.TUE);
        everyDay.add(DayOfWeek.WED);
        everyDay.add(DayOfWeek.THU);
        everyDay.add(DayOfWeek.FRI);
        everyDay.add(DayOfWeek.SAT);
        everyDay.add(DayOfWeek.SUN);
        showTimePicker(h,m,everyDay);
    }

    @Override
    public View loadView(View view) {

        //Load Imageview
        ImageView i = (ImageView) view.findViewById(R.id.new_image);

        i.setImageResource(R.drawable.no_image);

        if (mItem.getPicture() != null) {
            i.setImageBitmap(mItem.getPicture());
        }

        i.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
        i.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onAddImage();
            }
        });

        //Load ListView

        ListView lv = (ListView) view.findViewById(R.id.new_alarm_list);
        TextView txtEmpty = (TextView) view.findViewById(R.id.empty);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, mItem.getAlarms()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(mItem.getAlarms().get(position).getTime());
                text2.setText(mItem.getAlarms().get(position).printDays());
                return view;
            }
        };

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addAlarm();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete Alarm")
                        .setMessage("Do you want to remove this alarm?")

                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mItem.removeAlarm(mItem.getAlarms().get(position));
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
                return true;
            }
        });


        lv.setEmptyView(txtEmpty);
        lv.setAdapter(adapter);

        txtEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
        return view;
    }


    private void showTimePicker(int hour,int minute, ArrayList<DayOfWeek> dow) {
        // Create fragment and give it an argument specifying the article it should show
        Bundle args = new Bundle();
        args.putInt(TimePickerDialog.ARG_HOUR_ID, hour);
        args.putInt(TimePickerDialog.ARG_MINUTE_ID, minute);

        Iterator<DayOfWeek> dowIT = dow.iterator();
        ArrayList<Integer> dowINT = new ArrayList<Integer>();

        while (dowIT.hasNext()) {
            dowINT.add(dowIT.next().ordinal());
        }

        args.putIntegerArrayList(TimePickerDialog.ARG_DAYS_ID, dowINT);

        FragmentManager fm = getFragmentManager();
        TimePickerDialog timePick = TimePickerDialog.newInstance("Time Picker");
        timePick.setArguments(args);
        timePick.callback = this;
        timePick.show(fm, "time_picker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        Log.d(TAG,"OnActivityResult");
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    try {
                        Uri selectedImage = returnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(Uri.parse(selectedImage.toString()));
                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        Log.d("FILE", selectedImage.toString());

                        mItem.setPicture(bitmap);

                        ImageView i = (ImageView) findViewById(R.id.new_image);
                        Log.d("FILE", i.toString());
                        i.setImageBitmap(mItem.getPicture());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case SELECT_TIME:
                if (resultCode == Activity.RESULT_OK) {
                    int h = returnedIntent.getIntExtra(TimePickerDialog.ARG_HOUR_ID, 0);
                    int m = returnedIntent.getIntExtra(TimePickerDialog.ARG_MINUTE_ID, 0);
                    Alarm alarm = new Alarm(h,m);
                    alarm.setDays(returnedIntent.getIntegerArrayListExtra(TimePickerDialog.ARG_DAYS_ID));
                    mItem.addAlarm(alarm);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public void onFinnishTimePick(int hour, int minute, ArrayList<Integer> days) {
        Log.d(TAG,"Finished timepick");
        Alarm alarm = new Alarm(hour,minute);
        alarm.setDays(days);
        mItem.addAlarm(alarm);
        adapter.notifyDataSetChanged();
    }
}
