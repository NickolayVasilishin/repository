package com.catwithbat.mywarden;

/**
 * Created by n.vasilishin on 10.10.2015.
 */

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.catwithbat.mywarden.wardenutils.WorkDayRecord;

import java.util.Date;



public class WorkDayRecordTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public WorkDayRecord recordNow;
    public WorkDayRecord recordMorning;
    public WorkDayRecord recordTomorrow;
    public WorkDayRecord recordYesterday;
    public WorkDayRecord recordAWeekAgo;
    public WorkDayRecord recordAMonthAgo;
    public long hour = 1000*60*60;
    public long twentyFiveHours = hour*25;
    public long twelveHours = hour*12;


    public WorkDayRecordTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        recordNow = new WorkDayRecord();
        Date date = new Date(new Date().getTime() + twentyFiveHours);
        recordTomorrow = new WorkDayRecord(date);
        date = new Date(25000000);
        recordMorning = new WorkDayRecord(date);
        date = new Date(new Date().getTime() - 24 * hour);
        recordYesterday = new WorkDayRecord(date);
        date = new Date(new Date().getTime() - 7 * 24 * hour);
        recordAWeekAgo = new WorkDayRecord(date);
        date = new Date(new Date().getTime() - 31 * 24 * hour);
        recordAMonthAgo = new WorkDayRecord(date);
    }

    public void testIsToday() throws Exception{
        Log.e("TEST", "======" + recordNow);
        assertTrue(recordNow.isToday());
    }

    public void testIsTomorrow() throws Exception{
        assertFalse(recordTomorrow.isToday());
    }

    public void testCalculateHoursFromTwoDates() throws Exception {
        assertEquals("12:0", recordMorning.increaseTime(twelveHours).getHoursAsString());
    }

    public void testIsCurrentWeek(){
        assertTrue(recordNow.isCurrentWeek());
        assertFalse(recordAWeekAgo.isCurrentWeek());
        //assertFalse(recordYesterday.isCurrentWeek());
        //assertTrue(recordTomorrow.isCurrentWeek());
    }

    public void testIsCurrentMonth(){
        assertTrue(recordNow.isCurrentMonth());
        assertFalse(recordAMonthAgo.isCurrentMonth());
        //assertFalse(recordYesterday.isCurrentWeek());
        //assertTrue(recordTomorrow.isCurrentWeek());
    }



}
