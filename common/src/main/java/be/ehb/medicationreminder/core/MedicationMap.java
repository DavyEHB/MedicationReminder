package be.ehb.medicationreminder.core;

import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class MedicationMap extends TreeMap<Integer,Medication> {

    private static final String TAG = "MEDICATION_MAP";
    //private ArrayList<Medication> arMedList = null;
    private static MedicationMap instance = null;
/*
    static {
        // Add sample items.
        Medication m1 = new Medication(12,"Dafalgan");
        Medication m2 = new Medication(15,"Med 2");
        Medication m3 = new Medication(78,"Med 3");
        Medication m4 = new Medication(1,"Med 4");
        getInstance().put(m1.getID(), m1);
        getInstance().put(m2.getID(), m2);
        getInstance().put(m3.getID(), m3);
        getInstance().put(m4.getID(), m4);

        Alarms ts1 = new Alarms("17:05");
        Alarms ts2 = new Alarms("17:15");
        Alarms ts3 = new Alarms("16:05");
        Alarms ts4 = new Alarms("15:05");
        Alarms ts5 = new Alarms("16:18");
        Alarms ts6 = new Alarms("15:59");

        ts1.removeDay(DayOfWeek.MON);
        ts1.removeDay(DayOfWeek.MON);
        ts1.removeDay(DayOfWeek.SUN);

        m1.addAlarm(ts1);
        m1.addAlarm(ts2);
        m2.addAlarm(ts3);
        m2.addAlarm(ts4);
        m3.addAlarm(ts5);
        m4.addAlarm(ts6);
        m4.addAlarm(ts1);

    }
*/

    public static MedicationMap getInstance(){
        Log.d(TAG,"getInstance");
        if(instance == null){
            synchronized (MedicationMap.class) {
                if(instance == null){
                    instance = new MedicationMap();
                }
            }
        }
        return instance;
    }


    private MedicationMap(){
        //arMedList = new ArrayList<>();
        super();
    }


    /*
    public void addMedication(Medication medication){
        this.arMedList.add(medication);
    }

    public boolean deleteMedication(Medication medication){
        return arMedList.remove(medication);
    }
    */


    public Medication remove (Medication med)
    {
        return this.remove(med.getID());
    }


    public Medication get(String name){
        for (Entry<Integer,Medication> entry : this.entrySet()){
            if (entry.getValue().getName() == name){
                return entry.getValue();
            }
        }
        return null;
    }

    public TreeMap<Integer,Medication> checkAlarms(Alarms alarms){
        TreeMap<Integer,Medication> tList = new TreeMap<>();
        for (Entry<Integer,Medication> entry : this.entrySet()){
            if (entry.getValue().isTimeEqual(alarms)){
                tList.put(entry.getKey(),entry.getValue());
            }
        }
        return tList;
    }

    public void put(Medication mItem) {
        this.put(mItem.getID(),mItem);
    }

    public void putAll(ArrayList<Medication> medications)
    {
        for(Medication med : medications)
        {
            this.put(med);
        }
    }
}
