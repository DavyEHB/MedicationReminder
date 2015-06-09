package be.ehb.medicationreminder.core;


import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import be.ehb.medicationreminder.database.AbstractDatabaseObject;

/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class Alarms extends AbstractDatabaseObject{

   // private ArrayList<DayOfWeek> DOW;

    private ArrayList<DayOfWeek> DOW = new ArrayList<DayOfWeek>();

    private int iHours;
    private int iMinutes;
    private Medication parent = null;

    @SuppressWarnings("unused")
    private Alarms(){
        super(0);
    }

    public Alarms (int ID)
    {
        super(ID);
    }

    public Alarms(int ID, int hour, int minutes){
        super(ID);
        this.iHours = hour;
        this.iMinutes = minutes;
      }

	public Alarms(int hour, int minutes){
        super(0);
        iHours = hour;
        iMinutes = minutes;
        this.DOW = new ArrayList<>();
        this.DOW.add(DayOfWeek.MON);
        this.DOW.add(DayOfWeek.TUE);
        this.DOW.add(DayOfWeek.WED);
        this.DOW.add(DayOfWeek.THU);
        this.DOW.add(DayOfWeek.FRI);
        this.DOW.add(DayOfWeek.SAT);
        this.DOW.add(DayOfWeek.SUN);
    }


    public Alarms(String time){
        super(0);
        this.DOW = new ArrayList<>();
        this.DOW.add(DayOfWeek.MON);
        this.DOW.add(DayOfWeek.TUE);
        this.DOW.add(DayOfWeek.WED);
        this.DOW.add(DayOfWeek.THU);
        this.DOW.add(DayOfWeek.FRI);
        this.DOW.add(DayOfWeek.SAT);
        this.DOW.add(DayOfWeek.SUN);
        this.setTime(time);
    }

    public Alarms(String time, ArrayList<DayOfWeek> dow){
        super(0);
        this.DOW = dow;
        this.setTime(time);
    }

    public Alarms(int ID, String time, String dow, int parentID) {
        super(ID);
        this.setTime(time);
        this.setDays(dow);
        this.parent = MedicationList.getInstance().getMedicationByID(parentID);
    }

    public Alarms(int ID, String time, String dow, Medication med) {
        super(ID);
        this.setTime(time);
        this.setDays(dow);
        this.parent = med;
    }

    public void setDays(String dow) {
        String[] separated = dow.split(" - ");
        for (int i = 0;i< separated.length;i++)
        {
            Log.d("DOW", separated[i]);
            this.addDay(DayOfWeek.valueOf(separated[i]));
        }
    }

    public ArrayList<DayOfWeek> getDays(){
        return this.DOW;
    }

    public String printDays(){
        String str = "";
        Iterator IT = this.DOW.iterator();
        while (IT.hasNext()){

            if (str == ""){
                str = IT.next().toString();
            }
            else
            {
                str = str + " - " + IT.next().toString();
            }
        }
        return str;
    }
    
    public void addDay(DayOfWeek dow){
        if (!this.DOW.contains(dow)){
            this.DOW.add(dow);
        }
    }

    public void removeDay(DayOfWeek dow){
        this.DOW.remove(dow);
    }

    public String getTime(){
        return String.format("%02d",this.iHours) + ":" + String.format("%02d",this.iMinutes);
    }
    
    public void setTime(String time){
        String[] separated = time.split(":");
        iHours = Integer.valueOf(separated[0]);
        iMinutes = Integer.valueOf(separated[1]);
    }

    public void setTime(int hours,int minutes){
        this.iHours = hours;
        this.iMinutes = minutes;
    }

    public void setParent(Medication parent)
    {
        this.parent = parent;
    }

    public Medication getParent()
    {
        return this.parent;
    }

    public int getMinutes() {
        return iMinutes;
    }

    public void setMinutes(int Minutes) {
        this.iMinutes = Minutes;
    }

    public int getHours() {
        return iHours;
    }

    public void setHours(int Hours) {
        this.iHours = Hours;
    }

    public void setDays(ArrayList<Integer> days)
    {
        this.DOW.clear();
        Iterator<Integer> IT = days.iterator();
        while (IT.hasNext())
        {
            switch(IT.next()) {
                case 0:
                    this.DOW.add(DayOfWeek.MON);
                    break;
                case 1:
                    this.DOW.add(DayOfWeek.TUE);
                    break;
                case 2:
                    this.DOW.add(DayOfWeek.WED);
                    break;
                case 3:
                    this.DOW.add(DayOfWeek.THU);
                    break;
                case 4:
                    this.DOW.add(DayOfWeek.FRI);
                    break;
                case 5:
                    this.DOW.add(DayOfWeek.SAT);
                    break;
                case 6:
                    this.DOW.add(DayOfWeek.SUN);
                    break;
            }
        }
    }


    public String toString()
    {
        return getTime() + "\t" + printDays() + "\t" + this.parent.getID();
    }


}
