package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.DayOfWeek;

// ...

public class TimePickerDialog extends DialogFragment {

    private TimePicker mTimePick;

    private int iHour;
    private int iMinute;
    private ArrayList<Integer> sDays = new ArrayList<Integer>();

    public Callback callback;

    private List<ToggleButton> toggleButtons;
    private static final int[] BUTTON_IDS = {
            R.id.btn_mon,
            R.id.btn_tue,
            R.id.btn_wed,
            R.id.btn_thu,
            R.id.btn_fri,
            R.id.btn_sat,
            R.id.btn_sun,
     };

    public static final String ARG_MINUTE_ID = "minute_id";
    public static final String ARG_HOUR_ID = "hour_id";
    public static final String ARG_DAYS_ID = "days_id";

    public interface Callback {
        public void onFinnishTimePick(int hour, int minute, ArrayList<Integer> days);
    }

    public TimePickerDialog() {
        // Empty constructor required for DialogFragment
    }

    public static TimePickerDialog newInstance(String title) {
        TimePickerDialog frag = new TimePickerDialog();
        //frag.isModal = true; // WHEN FRAGMENT IS CALLED AS A DIALOG SET FLAG
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    private void sendResult() {
        sDays.clear();

        for (int id = 0; id <= 6; id++) {
            ToggleButton button = toggleButtons.get(id);
            Log.d("DAYS","Checking " + button.getText());
            Log.d("DAYS","Set " + button.isChecked());

            if (button.isChecked()) {
                sDays.add(id);
                Log.d("DAYS","Set " + button.getText());
            }
        }

        iHour = mTimePick.getCurrentHour();
        iMinute = mTimePick.getCurrentMinute();
        callback.onFinnishTimePick(iHour,iMinute,sDays);
/*
        Intent intent = new Intent();
        intent.putExtra(ARG_HOUR_ID, iHour);
        intent.putExtra(ARG_MINUTE_ID, iMinute);
        intent.putExtra(ARG_DAYS_ID, sDays);
        onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);
        //getTargetFragment().onActivityResult(
        //        getTargetRequestCode(), Activity.RESULT_OK, intent);
*/
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time_picker,null);
        String title = getArguments().getString("title", "Enter Name");
        iHour = getArguments().getInt(ARG_HOUR_ID);
        iMinute = getArguments().getInt(ARG_MINUTE_ID);
        sDays = getArguments().getIntegerArrayList(ARG_DAYS_ID);
       // mMedItem = MedicationList.getInstance().getMedicationByID(getArguments().getInt(ARG_ITEM_ID));
       // mTimeStamp = mMedItem.getAlarms().get(getArguments().getInt(ARG_ALARM_ID));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(view)
                .setTitle("Set Alarm")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            sendResult();
                        }
                });



        mTimePick = (TimePicker) view.findViewById(R.id.timePicker);
        mTimePick.setIs24HourView(true);

        mTimePick.setCurrentHour(iHour);
        mTimePick.setCurrentMinute(iMinute);

        Log.d("DAYS","Array " + sDays.toString());
        Log.d("DAYS","SUN " + String.valueOf(DayOfWeek.SUN.ordinal()));

        toggleButtons = new ArrayList<ToggleButton>(BUTTON_IDS.length);
        for(int id = 0; id <=6; id++) {
            ToggleButton button = (ToggleButton)view.findViewById(BUTTON_IDS[id]);
            if (sDays.contains(id)){
                Log.d("DAYS","Match " + button.getText());
                button.setChecked(true);
            }
            else
            {
                button.setChecked(false);
            }
            toggleButtons.add(button);
        }

        return alertDialogBuilder.create();
    }
}