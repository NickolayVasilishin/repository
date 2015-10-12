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
    private float hours;

    public WorkDayRecord(){
        Date current = new Date();
        date = new SimpleDateFormat(DATE_FORMAT).format(current);
        timeIn = timeOut = new SimpleDateFormat(TIME_FORMAT).format(current);
        hours = 0;
    }

    public WorkDayRecord(String date, String timeIn, String timeOut, float hours){
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hours = hours;
    }

    public WorkDayRecord(Date date){
        this.date = new SimpleDateFormat(DATE_FORMAT).format(date);
        timeIn = timeOut = new SimpleDateFormat(TIME_FORMAT).format(date);
        hours = 0;
    }

    public String getDate() {
        return date;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() { return timeOut; }

    public float getHours() { return hours; }

    /**
     * @return Time at work for week in "h:m" format
    * */
    public String getHoursAsString() {
        return "" + (int)hours  + ":" + (int)((hours - (int)hours) * 60 * 100);
    }

    public WorkDayRecord increaseTime(long time){
        try {
            timeOut = new SimpleDateFormat(TIME_FORMAT).format(new Date(new SimpleDateFormat(TIME_FORMAT).parse(timeOut).getTime()+time));
            hours = (new SimpleDateFormat(TIME_FORMAT).parse(timeOut).getTime() - new SimpleDateFormat(TIME_FORMAT).parse(timeIn).getTime()) / 1000/60/60;
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
        return date + " " + timeIn + " - " + timeOut + "; Worked for " + hours + " hours";
    }
}
