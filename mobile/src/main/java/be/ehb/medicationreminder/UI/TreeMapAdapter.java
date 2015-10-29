package be.ehb.medicationreminder.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.TreeMap;


import be.ehb.medicationreminder.core.Medication;

public class TreeMapAdapter extends ArrayAdapter<Medication> {
    private static final String TAG = "TREE_MAP_ADAPTER";
    private final Context context;
    private ArrayList<Medication> mValues = new ArrayList<Medication>();
    private ArrayList<Integer> mKeys = new ArrayList<Integer>();

    private TreeMap<Integer, Medication> mData = new TreeMap<Integer, Medication>();

    public TreeMapAdapter(Context context, int resource , int textViewResourceId, TreeMap<Integer, Medication> data) {
        super(context,resource,textViewResourceId,data.values().toArray(new Medication[data.size()]));
        mValues.addAll(data.values());
        this.context = context;
        mData  = data;
        mKeys.addAll(data.keySet());
    }
    public int getCount() {

        return mData.size();

    }

    public Medication getItem(int position) {
        mKeys.addAll(mData.keySet());
        return mData.get(mKeys.get(position));
    }


    public long getItemId(int arg0) {
        mKeys.addAll(mData.keySet());
        return mKeys.get(arg0);
    }
} 