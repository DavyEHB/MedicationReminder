package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class Alarm extends AbstractDatabaseObject{
    private static final String TAG = "ALARM";

    private ArrayList<DayOfWeek> DOW = new ArrayList<>();

    private int iHours;
    private int iMinutes;
    private int parentId = 0;

    @SuppressWarnings("unused")
    private Alarm(){
        super(0);
    }

    public Alarm(int ID)
    {
        super(ID);
    }

    public Alarm(int ID, int hour, int minutes){
        super(ID);
        this.iHours = hour;
        this.iMinutes = minutes;

      }
	public Alarm(int hour, int minutes){
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


    public Alarm(String time){
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

    public Alarm(String time, ArrayList<DayOfWeek> dow){
        super(0);
        this.DOW = dow;
        this.setTime(time);
    }

    public Alarm(int ID, String time, String dow, int parentID) {
        super(ID);
        this.setTime(time);
        this.setDays(dow);
        this.parentId = parentID;
    }

    public void setDays(String dow) {
        String[] separated = dow.split(" - ");
        for (int i = 0;i< separated.length;i++)
        {
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

    public void setParentID(int parentId)
    {
        this.parentId = parentId;
    }

    public int getParentID()
    {
        return this.parentId;
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
        return getTime() + "\t" + printDays() + "\t" + this.parentId;
    }

    public Calendar getNextAlarm(Calendar now)
    {

        int nextDay = 8;
        Calendar tTime = (Calendar) now.clone();

        //Covert to Monday = 1 Sunday = 7
        int day_of_week = now.get(Calendar.DAY_OF_WEEK);
        --day_of_week;
        if (day_of_week == 0) day_of_week = 7;

        tTime.set(Calendar.HOUR,iHours);
        tTime.set(Calendar.MINUTE,iMinutes);
        tTime.set(Calendar.SECOND,0);

        for (DayOfWeek dow : DOW)
        {
            int diffDay = dow.ordinal() + 1 - day_of_week;
            if(diffDay < 0){
                diffDay = diffDay + 7;
            }

            if (nextDay > diffDay) {
                nextDay = diffDay;
            }
        }

        if (tTime.before(now))
        {
            ++day_of_week;
            nextDay = 8;
            for (DayOfWeek dow : DOW)
            {
                int diffDay = dow.ordinal() + 1 - day_of_week;
                if(diffDay < 0){
                    diffDay = diffDay + 7;
                }

                if (nextDay > diffDay) {
                    nextDay = diffDay;
                }
            }
            ++nextDay;
        }
        tTime.add(Calendar.DAY_OF_WEEK,nextDay);
        return tTime;
    }
}
