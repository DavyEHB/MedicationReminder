package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import be.ehb.medicationreminder.MedicationReminder;
import be.ehb.medicationreminder.R;


import be.ehb.medicationreminder.core.Alarm;
import be.ehb.medicationreminder.core.DayOfWeek;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;

/**
 * A fragment representing a single Medication detail screen.
 * This fragment is either contained in a {@link MedicationListActivity}
 * in two-pane mode (on tablets) or a {@link MedicationDetailActivity}
 * on handsets.
 */
public class MedicationDetailFragment extends Fragment implements TimePickerDialog.Callback{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = "MedicationDetailFrag";
    //private static final String ARG_POS_ID = "alarm_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Medication mItem;

    private final int SELECT_PHOTO = 1;
    private final int ADD_ALARM = 2;
    private final int CHANGE_ALARM = 3;

    private ArrayAdapter adapter = null;

    private int listpos;

    private MedicationMap medMap = null;
    private MedicationReminder application;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MedicationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        application = (MedicationReminder) getActivity().getApplication();

        this.setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            //medMap = MedicationMap.getInstance();
            medMap = application.getMedicationMap();
            mItem = medMap.get(getArguments().getInt(ARG_ITEM_ID));
            //mItem = MedicationList.getInstance().getMedicationList().get(getArguments().getInt(ARG_ITEM_ID));
        }
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medication_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            // Instantiate an ImageView and define its properties


            ImageView i = (ImageView) rootView.findViewById(R.id.medication_image);

            i.setImageResource(R.drawable.no_image);

            if (mItem.getPicture() != null) {
                i.setImageBitmap(mItem.getPicture());
            }

            i.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
            i.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    return false;
                }
            });

            TextView tx = (TextView) rootView.findViewById(R.id.medication_name);
            tx.setText(mItem.getName());
            tx.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final EditText input = new EditText(getActivity());
                    input.setText(mItem.getName());
                    input.setSingleLine(true);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Medication name")
                            .setView(input)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    MedicationDAO medicationDAO = new MedicationDAO(getActivity());
                                    mItem.setName(input.getText().toString());
                                    medicationDAO.update(mItem);
                                    ((TextView) getView().findViewById(R.id.medication_name)).setText(mItem.getName());
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do nothing.
                        }
                    }).show();
                    return true;
                }
            });

            ListView lv = (ListView) rootView.findViewById(R.id.medication_alarms);

            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, mItem.getAlarms()) {
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

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showTimePicker(position);
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Delete Alarm")
                            .setMessage("Do you want to remove this alarm?")

                            .setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Alarm delAlarm = mItem.getAlarms().get(position);
                                            AlarmDAO alarmDAO = new AlarmDAO(getActivity());
                                            alarmDAO.delete(delAlarm);
                                            mItem.removeAlarm(delAlarm);
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
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    try {
                        final Uri imageUri = returnedIntent.getData();
                        Log.d("REQUEST", String.valueOf(imageUri));

                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        mItem.setPicture(selectedImage);

                        MedicationDAO medicationDAO = new MedicationDAO(getActivity());
                        medicationDAO.update(mItem);

                        ImageView i = (ImageView) getView().findViewById(R.id.medication_image);
                        i.setImageBitmap(mItem.getPicture());


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                break;

            case ADD_ALARM:
                if (resultCode == Activity.RESULT_OK) {
                    int h = returnedIntent.getIntExtra(TimePickerDialog.ARG_HOUR_ID, 0);
                    int m = returnedIntent.getIntExtra(TimePickerDialog.ARG_MINUTE_ID, 0);
                    Alarm alarm = new Alarm(h,m);
                    AlarmDAO alarmDAO = new AlarmDAO(getActivity());

                    alarm.setDays(returnedIntent.getIntegerArrayListExtra(TimePickerDialog.ARG_DAYS_ID));
                    mItem.addAlarm(alarm);
                    alarmDAO.insert(alarm);
                    adapter.notifyDataSetChanged();
                }
                break;
            case CHANGE_ALARM:
                if (resultCode == Activity.RESULT_OK) {
                    int h = returnedIntent.getIntExtra(TimePickerDialog.ARG_HOUR_ID, 0);
                    int m = returnedIntent.getIntExtra(TimePickerDialog.ARG_MINUTE_ID, 0);
                    Alarm alarm = mItem.getAlarms().get(listpos);
                    AlarmDAO alarmDAO = new AlarmDAO(getActivity());

                    alarm.setDays(returnedIntent.getIntegerArrayListExtra(TimePickerDialog.ARG_DAYS_ID));
                    alarm.setHours(h);
                    alarm.setMinutes(m);
                    alarmDAO.update(alarm);
                    adapter.notifyDataSetChanged();
                }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_alarm:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showTimePicker(int position){
        // Create fragment and give it an argument specifying the article it should show
        Bundle args = new Bundle();
        args.putInt(TimePickerDialog.ARG_HOUR_ID, mItem.getAlarms().get(position).getHours());
        args.putInt(TimePickerDialog.ARG_MINUTE_ID, mItem.getAlarms().get(position).getMinutes());
        listpos = position;

        Iterator<DayOfWeek> dowIT = mItem.getAlarms().get(position).getDays().iterator();
        ArrayList<Integer> dowINT = new ArrayList<Integer>();

        while(dowIT.hasNext()){
            dowINT.add(dowIT.next().ordinal());
        }

        args.putIntegerArrayList(TimePickerDialog.ARG_DAYS_ID, dowINT);

        FragmentManager fm = getFragmentManager();
        TimePickerDialog timePick = TimePickerDialog.newInstance("Time Picker");
        timePick.setArguments(args);
        timePick.setTargetFragment(MedicationDetailFragment.this,CHANGE_ALARM);
        timePick.callback = this;
        timePick.show(fm, "time_picker");
    }


    private void showTimePicker(int hour,int minute, ArrayList<DayOfWeek> dow){
        // Create fragment and give it an argument specifying the article it should show
        Bundle args = new Bundle();
        args.putInt(TimePickerDialog.ARG_HOUR_ID, hour);
        args.putInt(TimePickerDialog.ARG_MINUTE_ID, minute);

        Iterator<DayOfWeek> dowIT = dow.iterator();
        ArrayList<Integer> dowINT = new ArrayList<Integer>();

        while(dowIT.hasNext()){
            dowINT.add(dowIT.next().ordinal());
        }

        args.putIntegerArrayList(TimePickerDialog.ARG_DAYS_ID, dowINT);

        FragmentManager fm = getFragmentManager();
        TimePickerDialog timePick = TimePickerDialog.newInstance("Time Picker");
        timePick.setArguments(args);
        timePick.setTargetFragment(MedicationDetailFragment.this, ADD_ALARM);
        timePick.callback = this;
        timePick.show(fm, "time_picker");
    }



    @Override
    public void onFinnishTimePick(int hour, int minute, ArrayList<Integer> days) {
        Log.d(TAG, "Finished timepick");
        Alarm alarm = new Alarm(hour,minute);
        alarm.setDays(days);
        mItem.addAlarm(alarm);
        adapter.notifyDataSetChanged();
    }
}
