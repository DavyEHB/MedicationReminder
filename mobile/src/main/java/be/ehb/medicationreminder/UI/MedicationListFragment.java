package be.ehb.medicationreminder.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Iterator;
import java.util.Map;

import be.ehb.medicationreminder.MedicationReminder;
import be.ehb.medicationreminder.R;


import be.ehb.medicationreminder.core.Alarm;
import be.ehb.medicationreminder.core.Medication;
import be.ehb.medicationreminder.core.MedicationMap;
import be.ehb.medicationreminder.database.AlarmDAO;
import be.ehb.medicationreminder.database.MedicationDAO;

/**
 * A list fragment representing a list of Medications. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MedicationDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MedicationListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String TAG = "MED_LIST_FRAG";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private ArrayAdapter adapter = null;

    private MedicationMap medMap = null;

    private MedicationReminder application;

/*
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("LONG", "Long CLIkced");
        return true;
    }*/

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(int id);
        //public void onAddMedicationFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MedicationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        application = (MedicationReminder) getActivity().getApplication();
        medMap = application.getMedicationMap();

/*
        medMap = MedicationMap.getInstance();
        MedicationDAO medicationDAO = new MedicationDAO(getActivity());
        AlarmDAO alarmDAO = new AlarmDAO(getActivity());
        Log.d(TAG,"Loading Database");
        medMap.putAll(medicationDAO.getAll());

        for (Map.Entry<Integer,Medication> entry : medMap.entrySet())
        {
            Medication tMed = entry.getValue();
            tMed.addAlarms(alarmDAO.getAlarmsByMed(tMed));
        }
*/
        //application.setMedicationMap(medMap);

/*
        Iterator<Medication> medIT = MedicationList.getInstance().getMedicationList().iterator();
        while (medIT.hasNext())
        {
            Medication nextMed = medIT.next();
            nextMed.addAlarms(alarmDAO.getAlarmsByMed(nextMed));
        }
*/
        /*
        try {
            JSONObject json = AlarmJSON.getJSON(MedicationList.getInstance().getMedicationList().get(0).getAlarms());
            Log.d("JSON", json.toString() );
            Log.d("JSON", AlarmJSON.getList(json).get(0).printDays());
        } catch (JSONException e) {
            e.printStackTrace();
        }

*/
        adapter = new TreeMapAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                medMap
                );
        setListAdapter(adapter);
/*
        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,medMap.values().toArray(new Medication[medMap.size()]));
                //medMap.getInstance().getMedicationList());
        setListAdapter(adapter);
*/
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                Log.d(TAG, "Item position: " + String.valueOf(position));
                Log.d(TAG, "Item id: " + String.valueOf(id));
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Delete Medication")
                        .setMessage("Do you want to remove this medication?")

                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Medication delMed = MedicationList.getInstance().getMedicationList().get(position);
                                        Medication delMed = medMap.get((int) id);
                                        MedicationDAO medicationDAO = new MedicationDAO(getActivity());
                                        AlarmDAO alarmDAO = new AlarmDAO(getActivity());
                                        Iterator<Alarm> delAlarmsIT = delMed.getAlarms().iterator();
                                        while (delAlarmsIT.hasNext())
                                        {
                                            alarmDAO.delete(delAlarmsIT.next());
                                        }
                                        medicationDAO.delete(delMed);
                                        medMap.remove(delMed);

                                        //MedicationList.getInstance().deleteMedication(delMed);
                                        adapter = new TreeMapAdapter(
                                                getActivity(),
                                                android.R.layout.simple_list_item_activated_1,
                                                android.R.id.text1,
                                                medMap
                                        );
                                        setListAdapter(adapter);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
       // mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected((int)id);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_med:
                Log.d("NEW", "NEW MEDS");
                //mCallbacks.onAddMedicationFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        adapter = new TreeMapAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                medMap
        );
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }


    public void onSyncWatchClick(View view){
        Log.d("MENU","Sync watch");

    }
}
