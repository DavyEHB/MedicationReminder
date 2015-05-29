package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Iterator;

import be.ehb.medicationreminder.database.AbstractDatabaseObject;

public class Medication extends AbstractDatabaseObject {

	private String sName;
	private String sPicture;
    private final ArrayList<Alarms> aAlarms = new ArrayList<>();

    @SuppressWarnings("unused")
	private Medication(){
        super(0);
    }

    public Medication (String Name){
        super(0);
        this.sName = Name;
    }

    public Medication (int ID,String Name){
        super(ID);
        this.sName = Name;
    }

    public Medication (int ID,String Name, String Picture)
    {
        super(ID);
        this.sName = Name;
        this.sPicture = Picture;
    }

    public String getName() {
        return sName;
    }

    public void setName(String name) {
        sName = name;
    }

    public ArrayList<Alarms> getAlarms(){
        return aAlarms;
    }

    public String getPicture() {
        return sPicture;
    }

    public void setPicture(String picture) {
        sPicture = picture;
    }

    public void addAlarm(Alarms alarm){
        alarm.setParent(this);
        this.aAlarms.add(alarm);
    }

    public void addAlarms(ArrayList<Alarms> alarms){
        Iterator<Alarms> alIT = alarms.iterator();
        while (alIT.hasNext()){
            this.addAlarm(alIT.next());
        }
    }

    public void addAlarm(String time){
        Alarms tsTemp = new Alarms(time);
        this.addAlarm(tsTemp);
    }

    public void addAlarm(ArrayList<DayOfWeek> dow, String time){
        Alarms tsTemp = new Alarms(time,dow);
        this.addAlarm(tsTemp);
    }

    public void removeAlarm(Alarms alarm)
    {
        alarm.setParent(null);
        this.aAlarms.remove(alarm);
    }


    public boolean isTimeEqual(Alarms timestamp){
        Iterator<Alarms> itTime = aAlarms.iterator();
        while (itTime.hasNext()){
            if (itTime.next().equals(timestamp)){
                return true;
            }
        }
        return false;
    }
    
    public String toString(){
        /*
    	String sTemp = "ID: ";
    	sTemp = sTemp + this.getID() + "\n";
    	sTemp = sTemp + "\tName: " + this.sName + "\n";
    	sTemp = sTemp + "\tPicture: " + this.sPicture;
    	return sTemp;
    	*/


        return sName;
    }
}