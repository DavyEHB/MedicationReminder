package be.ehb.medicationreminder.core;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class Medication extends AbstractDatabaseObject {

    private static final int MAX_WIDTH = 600 ;
    private static final String TAG = "Medication";
    private String sName;
	private Bitmap Picture;
    private final ArrayList<Alarm> aAlarms = new ArrayList<Alarm>();

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

    public ArrayList<Alarm> getAlarms(){
        return aAlarms;
    }

    public Bitmap getPicture() {
        return Picture;
    }

    public void setPicture(Bitmap picture) {
        Picture = getResizedBitmap(picture,MAX_WIDTH);
    }

    public void addAlarm(Alarm alarm){
        alarm.setParentID(this.getID());
        this.aAlarms.add(alarm);
    }

    public void addAlarms(ArrayList<Alarm> alarms){
        Iterator<Alarm> alIT = alarms.iterator();
        while (alIT.hasNext()){
            this.addAlarm(alIT.next());
        }
    }

    public void removeAlarm(Alarm alarm)
    {
        alarm.setParentID(0);
        this.aAlarms.remove(alarm);
    }


    public boolean isTimeEqual(Alarm timestamp){
        Iterator<Alarm> itTime = aAlarms.iterator();
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

    public Calendar getNextAlarm(Calendar now)
    {
        Calendar next = null;
        Alarm nextAlarm = null;
        for (Alarm alarm : this.aAlarms)
        {
            Calendar temp = alarm.getNextAlarm(now);
            if (next == null)
            {
                next = temp;
                nextAlarm = alarm;
            }
            if (temp.before(next))
            {
                next = temp;
            }
        }
        Log.d(TAG,"Get Next alarm: " + next.getTime());
        return next;
    }
}