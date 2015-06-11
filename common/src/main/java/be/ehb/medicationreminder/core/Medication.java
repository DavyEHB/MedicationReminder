package be.ehb.medicationreminder.core;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.Iterator;


public class Medication extends AbstractDatabaseObject {

    private static final int MAX_WIDTH = 600 ;
    private String sName;
	private Bitmap Picture;
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

    public Medication (int ID,String Name, Bitmap picture)
    {
        super(ID);
        this.sName = Name;
        this.Picture = picture;
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

    public Bitmap getPicture() {
        return Picture;
    }

    public void setPicture(Bitmap picture) {
        Picture = getResizedBitmap(picture,MAX_WIDTH);
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
    	sTemp = sTemp + "\tPicture: " + this.Picture;
    	return sTemp;
    	*/


        return sName;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int maxWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = ((float) maxWidth) / width;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scale, scale);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}