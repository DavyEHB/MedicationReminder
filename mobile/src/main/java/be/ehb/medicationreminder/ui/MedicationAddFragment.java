package be.ehb.medicationreminder.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.Alarms;
import be.ehb.medicationreminder.core.DayOfWeek;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationList;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;

public class MedicationAddFragment extends Fragment {

    private final int SELECT_PHOTO = 1;
    private final int SELECT_TIME = 2;

    private OnFragmentInteractionListener mListener;
    private Medication mItem = null;

    private ArrayAdapter adapter = null;

    public MedicationAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItem = new Medication("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_medication_add, container, false);

        Button add = (Button) rootView.findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItem.getName() != ""){
                    MedicationDAO medDAO = new MedicationDAO(getActivity());
                    AlarmDAO alarmDAO = new AlarmDAO(getActivity());
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
                    mListener.onButtonClicked();
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Medication name is empty", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem = null;
                mListener.onButtonClicked();
            }
        });

        TextView tx = (TextView) rootView.findViewById(R.id.new_med_name);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());
                input.setSingleLine(true);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Medication name")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mItem.setName(input.getText().toString());
                                ((TextView) getView().findViewById(R.id.new_med_name)).setText(mItem.getName());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            }
        });


        ImageView i = (ImageView) rootView.findViewById(R.id.new_image);

        i.setImageResource(R.drawable.no_image);

        if (mItem.getPicture() != null) {
            String uri = mItem.getPicture();
            final InputStream imageStream;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(Uri.parse(uri));
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                i.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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

        ListView lv = (ListView) rootView.findViewById(R.id.new_alarm_list);
        TextView txtEmpty = (TextView) rootView.findViewById(R.id.empty);

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addAlarm();
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

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
           public void onButtonClicked();
    }

    private void addAlarm(){
        Log.d("NEW", "NEW ALARM FOR " + mItem.getName());
        int h = Calendar.getInstance().get(Calendar.HOUR);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        ArrayList<DayOfWeek> everyDay = new ArrayList<>();
        everyDay.add(DayOfWeek.MON);
        everyDay.add(DayOfWeek.TUE);
        everyDay.add(DayOfWeek.WED);
        everyDay.add(DayOfWeek.THU);
        everyDay.add(DayOfWeek.FRI);
        everyDay.add(DayOfWeek.SAT);
        everyDay.add(DayOfWeek.SUN);
        showTimePicker(h,m,everyDay);
    }

    private void showTimePicker(int hour,int minute, ArrayList<DayOfWeek> dow){
        // Create fragment and give it an argument specifying the article it should show
        Bundle args = new Bundle();
        args.putInt(TimePickerDialog.ARG_HOUR_ID, hour);
        args.putInt(TimePickerDialog.ARG_MINUTE_ID, minute);

        Iterator<DayOfWeek> dowIT = dow.iterator();
        ArrayList<Integer> dowINT = new ArrayList<>();

        while(dowIT.hasNext()){
            dowINT.add(dowIT.next().ordinal());
        }

        args.putIntegerArrayList(TimePickerDialog.ARG_DAYS_ID, dowINT);

        FragmentManager fm = getFragmentManager();
        TimePickerDialog timePick = TimePickerDialog.newInstance("Time Picker");
        timePick.setArguments(args);
        timePick.setTargetFragment(MedicationAddFragment.this,SELECT_TIME);
        timePick.show(fm, "time_picker");
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
                        mItem.setPicture(imageUri.toString());

                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(Uri.parse(mItem.getPicture()));
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        ImageView i = (ImageView) getView().findViewById(R.id.new_image);
                        i.setImageBitmap(selectedImage);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                break;

            case SELECT_TIME:
                if (resultCode == Activity.RESULT_OK) {
                    int h = returnedIntent.getIntExtra(TimePickerDialog.ARG_HOUR_ID, 0);
                    int m = returnedIntent.getIntExtra(TimePickerDialog.ARG_MINUTE_ID, 0);
                    Alarms alarm = new Alarms(h,m);
                    alarm.setDays(returnedIntent.getIntegerArrayListExtra(TimePickerDialog.ARG_DAYS_ID));
                    mItem.addAlarm(alarm);
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
