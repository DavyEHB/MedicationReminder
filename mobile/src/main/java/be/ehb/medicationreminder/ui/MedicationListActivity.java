package be.ehb.medicationreminder.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;


import be.ehb.medicationreminder.R;
import be.ehb.medicationreminder.core.MedicationList;

/**
 * An activity representing a list of Medications. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MedicationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MedicationListFragment} and the item details
 * (if present) is a {@link MedicationDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MedicationListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MedicationListActivity extends Activity
        implements MedicationListFragment.Callbacks{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private MedicationListFragment listFrag = null;
    private MedicationAddFragment addFrag = null;

    public static final int ADD_NEW_MED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        if (findViewById(R.id.medication_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            listFrag = ((MedicationListFragment) getFragmentManager()
                    .findFragmentById(R.id.medication_list));
            listFrag.setHasOptionsMenu(true);

        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link MedicationListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(MedicationDetailFragment.ARG_ITEM_ID, id);
            MedicationDetailFragment fragment = new MedicationDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.medication_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MedicationDetailActivity.class);
            detailIntent.putExtra(MedicationDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onAddMedicationFragment() {
        Log.d("NEW MED","Starting new activity");
        Intent detailIntent = new Intent(this, MedicationAddActivity.class);
        startActivityForResult(detailIntent, ADD_NEW_MED);
    }
}
