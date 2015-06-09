package be.ehb.medicationreminder.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.ehb.medicationreminder.database.AbstractDatabaseObject;

/**
 * Created by davy.van.belle on 2/06/2015.
 */
public interface JSONParser {

    public abstract JSONObject getJSON(ArrayList<AbstractDatabaseObject> items) throws JSONException;
    public abstract ArrayList<AbstractDatabaseObject> getList(JSONObject jsonObject);

}