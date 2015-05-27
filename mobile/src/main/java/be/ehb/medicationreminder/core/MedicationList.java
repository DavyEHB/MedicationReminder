package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class MedicationList{

    private ArrayList<Medication> arMedList = null;
    private static MedicationList instance = null;

    static {
        // Add 3 sample items.
        Medication m1 = new Medication("Dafalgan",12);
        Medication m2 = new Medication("Med 2",15);
        Medication m3 = new Medication("Med 3",78);
        Medication m4 = new Medication("Med 4",1);
        getInstance().addMedication(m1);
        getInstance().addMedication(m2);
        getInstance().addMedication(m3);
        getInstance().addMedication(m4);

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


        /*
        addItem(new DummyItem("1", "Item 1"));
        addItem(new DummyItem("2", "Item 2"));
        addItem(new DummyItem("3", "Item 3"));
        */
    }

    public static MedicationList getInstance(){
        if(instance == null){
            synchronized (MedicationList.class) {
                if(instance == null){
                    instance = new MedicationList();
                }
            }
        }
        return instance;
    }

    public static MedicationList getInstance(ArrayList<Medication> medications){
        if(instance == null){
            synchronized (MedicationList.class) {
                if(instance == null){
                    instance = new MedicationList(medications);
                }
            }
        }
        return instance;
    }

    private MedicationList(){
        arMedList = new ArrayList<>();
    }

    private MedicationList(ArrayList<Medication> meds){
        arMedList = meds;
    }

    public void addMedication(Medication medication){
        this.arMedList.add(medication);
    }

    public boolean deleteMedication(Medication medication){
        return arMedList.remove(medication);
    }

    public boolean deleteMedication(String name){
        return arMedList.remove(this.getMedicationByName(name));
    }

    public boolean deleteMedication(int ID){
        return arMedList.remove(this.getMedicationByID(ID));
    }

    public ArrayList<Medication> getMedicationList(){
        return this.arMedList;
    }

    public void setMedicationList(ArrayList<Medication> medications){
        this.arMedList = medications;
    }

    Medication getMedicationByName(String name){
        Iterator<Medication> MedIt = arMedList.iterator();
        while (MedIt.hasNext()){
            Medication mNext = MedIt.next();
            if (mNext.getName().equals(name)){
                return mNext;
            }
        }
        return null;
    }

    Medication getMedicationByID(int ID){
        Iterator<Medication> MedIt = arMedList.iterator();
        while (MedIt.hasNext()){
            Medication mNext = MedIt.next();
            if (mNext.getID() == ID){
                return mNext;
            }
        }
        return null;
    }

    public ArrayList<Medication> checkAlarms(Alarms Alarms){
    	ArrayList<Medication> tList = new ArrayList<Medication>();
        Iterator<Medication> MedIt = arMedList.iterator();
        while (MedIt.hasNext()){
            Medication mNext = MedIt.next();
            if (mNext.isTimeEqual(Alarms)){
                tList.add(mNext);
            }
        }
        return tList;
    }
}
