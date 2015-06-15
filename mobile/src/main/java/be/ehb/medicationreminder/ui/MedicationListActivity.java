package be.ehb.medicationreminder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;


import be.ehb.medicationreminder.R;

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
            MedicationListFragment listFrag;
            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            listFrag = ((MedicationListFragment) getFragmentManager()
                    .findFragmentById(R.id.medication_list));
            listFrag.setHasOptionsMenu(true);

        }
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
        Intent addIntent = new Intent(this, MedicationAddActivity.class);
        startActivityForResult(addIntent, MedicationAddActivity.ADD_NEW_MED);
    }

}