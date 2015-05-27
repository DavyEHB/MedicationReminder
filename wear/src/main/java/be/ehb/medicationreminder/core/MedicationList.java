package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class MedicationList{

    private ArrayList<Medication> arMedList = new ArrayList<>();
    private static MedicationList instance = null;

    private MedicationList(){}

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

    public Medication getMedicationByName(String name){
        Iterator<Medication> MedIt = arMedList.iterator();
        while (MedIt.hasNext()){
            Medication mNext = MedIt.next();
            if (mNext.getName().equals(name)){
                return mNext;
            }
        }
        return null;
    }

    public Medication getMedicationByID(int ID){
        Iterator<Medication> MedIt = arMedList.iterator();
        while (MedIt.hasNext()){
            Medication mNext = MedIt.next();
            if (mNext.getID() == ID){
                return mNext;
            }
        }
        return null;
    }

    public ArrayList<Medication> checkTimeStamps(TimeStamp timestamp){
    	ArrayList<Medication> tList = new ArrayList<Medication>();
        Iterator<Medication> MedIt = arMedList.iterator();
        while (MedIt.hasNext()){
            Medication mNext = MedIt.next();
            if (mNext.isTimeEqual(timestamp)){
                tList.add(mNext);
            }
        }
        return tList;
    }
}
