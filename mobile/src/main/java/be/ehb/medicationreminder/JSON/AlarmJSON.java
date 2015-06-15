package be.ehb.medicationreminder.mobile.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import be.ehb.medicationreminder.core.Alarms;


/**
 * Created by davy.van.belle on 2/06/2015.
 */
public class AlarmJSON {

    public static JSONObject getJSON(ArrayList<Alarms> items) throws JSONException {
        Iterator<Alarms> listIT = items.iterator();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        while (listIT.hasNext())
        {

            Alarms alarm = listIT.next();
            JSONObject val = new JSONObject();
            val.put("id", alarm.getID());
            val.put("time",alarm.getTime());
            val.put("days", alarm.printDays());

            jsonArray.put(val);

        }

        jsonObject.put("alarms", jsonArray);
        return jsonObject;
    }


    public static ArrayList<Alarms> getList(JSONObject jsonObject) throws JSONException {
        ArrayList<Alarms> alarms = new ArrayList<>();
        JSONObject tmpObject = null;
        Alarms tmpAlarm = null;
        JSONArray jsonArray = jsonObject.getJSONArray("alarms");
        for (int i = 0 ; i<jsonArray.length();i++)
        {
            tmpObject = jsonArray.getJSONObject(i);
            tmpAlarm = new Alarms(tmpObject.getInt("id"));
            tmpAlarm.setTime(tmpObject.getString("time"));
            tmpAlarm.setDays(tmpObject.getString("days"));
            alarms.add(tmpAlarm);
        }
        return alarms;
    }
}
