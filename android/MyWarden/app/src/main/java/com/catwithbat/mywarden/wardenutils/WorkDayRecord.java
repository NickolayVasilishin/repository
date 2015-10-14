package com.catwithbat.mywarden.wardenutils;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by n.vasilishin on 05.10.2015.
 */
public class WorkDayRecord {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String TIME_FORMAT = "H:m:s";

    //YYYY-MM-dd
    private String date;
    //H:m:s
    private String timeIn;
    //H:m:s
    private String timeOut;
//    private float hours;
    private float time;

    public WorkDayRecord(){
        Date current = new Date();
        date = new SimpleDateFormat(DATE_FORMAT).format(current);
        timeIn = timeOut = new SimpleDateFormat(TIME_FORMAT).format(current);
        time = 0;
    }

    public WorkDayRecord(WorkDayRecord record) {
        this.date = record.date;
        this.timeIn = record.timeIn;
        this.timeOut = record.timeOut;
        this.time = record.time;
    }

    public WorkDayRecord(String date, String timeIn, String timeOut, float time){
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.time = time;
    }

    public WorkDayRecord(Date date){
        this.date = new SimpleDateFormat(DATE_FORMAT).format(date);
        timeIn = timeOut = new SimpleDateFormat(TIME_FORMAT).format(date);
        time = 0;
    }

    public String getDate() {
        return date;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() { return timeOut; }
    //TODO
//    public float getHours() { return 1; }
    public float getTime() { return  time; }

    /**
     * @return Time at work for week in "h:m" format
    * */
    public String getHoursAsString() {
//        return "12:00h";
        return "" + (int)time/1000/60/60 + ":" + ((int)time/1000/60)%60;
    }

    public WorkDayRecord increaseTime(WorkDayRecord newRecord){
        try {
            newRecord.timeIn = this.timeIn;
            newRecord.date = this.date;
            newRecord.time = (new SimpleDateFormat(TIME_FORMAT).parse(newRecord.timeOut).getTime() - new SimpleDateFormat(TIME_FORMAT).parse(this.timeIn).getTime());
//            newRecord.time = newRecord.time ; // /1000/60/60
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newRecord;
    }

    public WorkDayRecord increaseTime(long time){
        try {
            timeOut = new SimpleDateFormat(TIME_FORMAT).format(new Date(new SimpleDateFormat(TIME_FORMAT).parse(timeOut).getTime()+time));
            this.time = new SimpleDateFormat(TIME_FORMAT).parse(timeOut).getTime() - new SimpleDateFormat(TIME_FORMAT).parse(timeIn).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isToday(){
        return new SimpleDateFormat(DATE_FORMAT).format(new Date()).equals(date);
    }

    public boolean isCurrentWeek(){
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(new SimpleDateFormat(WorkDayRecord.DATE_FORMAT).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new GregorianCalendar().get(Calendar.WEEK_OF_MONTH) == calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public boolean isCurrentMonth(){
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(new SimpleDateFormat(WorkDayRecord.DATE_FORMAT).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new GregorianCalendar().get(Calendar.MONTH) == calendar.get(Calendar.MONTH);
    }

    @Override
    public String toString(){
        return date + " " + timeIn + " - " + timeOut + "; Worked for " + (int)time/1000/60/60 + " hours " + ((int)time/1000/60)%60 + " minutes";
    }
}
