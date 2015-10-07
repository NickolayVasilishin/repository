package com.sample.mywarden.wardenutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by n.vasilishin on 05.10.2015.
 */
public class WorkDayRecord {
    //YYYY-MM-dd
    private String date;
    //H:m:s
    private String timeIn;
    //H:m:s
    private String timeOut;
   //H:m
    private int hours;
    private int remainingHours;

    public WorkDayRecord(){
        Date current = new Date();
        date = new SimpleDateFormat("YYYY-MM-dd").format(current);
        timeIn = timeOut = new SimpleDateFormat("H:m:s").format(current);
        hours = 0;
    }

    public WorkDayRecord(String date, String timeIn, String timeOut, int hours){
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hours = hours;
    }

    public String getDate() {
        return date;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public int getHours() {
        return hours;
    }

    public WorkDayRecord increaseTime(long time){
        try {
            timeOut = new SimpleDateFormat("H:m:s").format(new Date(new SimpleDateFormat("H:m:s").parse(timeOut).getTime()+time));
            hours = Integer.parseInt(new SimpleDateFormat("H").format(new SimpleDateFormat("H:m:s").parse(timeOut).getTime() - new SimpleDateFormat("H:m:s").parse(timeIn).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isToday(){
        return new java.text.SimpleDateFormat("YYYY-MM-dd").format(new Date()) == date ? true : false;
    }
}
