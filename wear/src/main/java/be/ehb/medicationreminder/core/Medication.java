package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Iterator;

public class Medication {

	private int iID;
	private String sName;
	private String sPicture;
    private final ArrayList<TimeStamp> tsTimes = new ArrayList<>();

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

    public String getPicture() {
        return sPicture;
    }

    public void setPicture(String picture) {
        sPicture = picture;
    }

    public void addTimeStamp(TimeStamp time){
        this.tsTimes.add(time);
    }

    public void addTimeStamp(DayOfWeek dow, String time){
        TimeStamp tsTemp = new TimeStamp(dow,time);
        this.tsTimes.add(tsTemp);
    }

    public boolean isTimeEqual(TimeStamp timestamp){
        Iterator<TimeStamp> itTime = tsTimes.iterator();
        while (itTime.hasNext()){
            if (itTime.next().equals(timestamp)){
                return true;
            }
        }
        return false;
    }
    
    public String toString(){
    	String sTemp = "ID: ";
    	sTemp = sTemp + this.iID + "\n";
    	sTemp = sTemp + "\tName: " + this.sName + "\n";
    	sTemp = sTemp + "\tPicture: " + this.sPicture;
    	return sTemp;
    }
}