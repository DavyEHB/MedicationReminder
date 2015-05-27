package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Iterator;

public class Medication {

	private int iID;
	private String sName;
	private String sPicture;
    private final ArrayList<Alarms> aAlarms = new ArrayList<>();

    @SuppressWarnings("unused")
	private Medication(){}

    public Medication (String Name){
        this.sName = Name;
        this.iID  = 0;
    }

    public Medication (String Name, int ID){
        this.sName = Name;
        this.iID  = ID;
    }
    
    public int getID(){
        return iID;
    }
    
    public void setID(int ID){
    	this.iID = ID;
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
        this.aAlarms.add(alarm);
    }

    public void addAlarm(String time){
        Alarms tsTemp = new Alarms(time);
        this.aAlarms.add(tsTemp);
    }

    public void addAlarm(ArrayList<DayOfWeek> dow, String time){
        Alarms tsTemp = new Alarms(time,dow);
        this.aAlarms.add(tsTemp);
    }

    public void removeAlarm(Alarms alarm)
    {
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
    	/*String sTemp = "ID: ";
    	sTemp = sTemp + this.iID + "\n";
    	sTemp = sTemp + "\tName: " + this.sName + "\n";
    	sTemp = sTemp + "\tPicture: " + this.sPicture;
    	return sTemp;
    	*/

        return sName;
    }
}