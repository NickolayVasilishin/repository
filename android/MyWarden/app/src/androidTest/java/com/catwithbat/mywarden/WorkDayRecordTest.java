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
    public long twentyFiveHours = 1000*60*60*25;
    public long twelveHours = 1000*60*60*12;

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
    }

    public void testIsToday() throws Exception{
        Log.e("TEST", "======" + recordNow);
        assertTrue(recordNow.isToday());
    }

    public void testIsTommorow() throws Exception{
        assertFalse(recordTomorrow.isToday());
    }

    public void testCalculateHoursFromTwoDates() throws Exception {
        System.out.println("=====================================");
        assertEquals("12:0", recordMorning.increaseTime(twelveHours).getHours());
    }
}
