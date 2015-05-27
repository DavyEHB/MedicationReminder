package be.ehb.medicationreminder.core;


/**
 * Created by davy.van.belle on 6/05/2015.
 */
public class TimeStamp {

    private DayOfWeek DOW;
    private String sTime;

    @SuppressWarnings("unused")
	private TimeStamp(){}

    public TimeStamp(DayOfWeek dow, String time){
        DOW = dow;
        sTime = time;
    }

    public DayOfWeek getDayOfWeek(){
        return this.DOW;
    }
    
    public void setDOW(DayOfWeek dow){
    	DOW = dow;
    }

    public String getTime(){
        return this.sTime;
    }
    
    public void setTime(String time){
    	sTime = time;
    }
    
    private boolean equals(DayOfWeek dow){
        return (this.DOW == dow)||(dow == DayOfWeek.ALL);
    }

    private boolean equals(String time){
        return (this.sTime == time);
    }

    public boolean equals(TimeStamp timestamp){
        return (timestamp.equals(this.sTime) && timestamp.equals(this.DOW));
    }
    
    
}
