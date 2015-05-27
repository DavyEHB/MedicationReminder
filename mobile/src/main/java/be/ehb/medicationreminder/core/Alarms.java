package be.ehb.medicationreminder.core;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class Alarms{

   // private ArrayList<DayOfWeek> DOW;

    private ArrayList<DayOfWeek> DOW = new ArrayList<DayOfWeek>();

    private int iHours;
    private int iMinutes;
    private int id;

    @SuppressWarnings("unused")
    private Alarms(){}

	public Alarms(int hour, int minutes){
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
        this.DOW = dow;
        this.setTime(time);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return getTime() + "\t" + DOW;
    }
    
    
}
