package com.catwithbat.mywarden;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.catwithbat.mywarden.wardenutils.WorkDayRecord;
import com.catwithbat.mywarden.wardenutils.database.WardenDataBase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by n.vasilishin on 12.10.2015.
 */
public class WardenDataBaseTest extends AndroidTestCase{
    private WardenDataBase database;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        database = new WardenDataBase(context, WardenDataBase.DATABASE_NAME, null, WardenDataBase.DATABASE_VERSION);
    }

    @Override
    public void tearDown() throws Exception {
        database.close();
        super.tearDown();
    }

    public void testIsCreated(){
        assertTrue(database != null);
    }

    public void testPersistRecord() throws Exception {
        WorkDayRecord wr = new WorkDayRecord();
        assertNotNull(database.persistRecord(wr));
        assertNotNull(database.getLastRecord());

        assertEquals(wr.getTimeIn(), database.getLastRecord().getTimeIn());
    }

    public void testIsLastRecordToday(){
        WorkDayRecord recordYesterday = new WorkDayRecord(new Date(new Date().getTime() - 1000*60*60*24)).increaseTime(100000);
        WorkDayRecord recordToday = new WorkDayRecord().increaseTime(100000);
        WorkDayRecord recordTomorrow = new WorkDayRecord(new Date(new Date().getTime() + 1000*60*60*24)).increaseTime(100000);

        database.persistRecord(recordYesterday);
        assertFalse(recordYesterday.getDate().equals(database.getLastRecordTodayOrCreateNew().getDate()));

        database.persistRecord(recordToday);
        assertTrue(recordToday.getDate().equals(database.getLastRecordTodayOrCreateNew().getDate()));

        database.persistRecord(recordTomorrow);
        assertFalse(recordTomorrow.getDate().equals(database.getLastRecordTodayOrCreateNew().getDate()));
    }

    public void testGetLastRecord() throws Exception {
        WorkDayRecord recordYesterday = new WorkDayRecord(new Date(new Date().getTime() - 1000*60*60*24)).increaseTime(100000);
        WorkDayRecord recordToday = new WorkDayRecord().increaseTime(100000);
        WorkDayRecord recordTomorrow = new WorkDayRecord(new Date(new Date().getTime() + 1000*60*60*24)).increaseTime(100000);

        database.persistRecord(recordYesterday);
        assertTrue(recordYesterday.getDate().equals(database.getLastRecord().getDate()));

        database.persistRecord(recordToday);
        assertTrue(recordToday.getDate().equals(database.getLastRecord().getDate()));

        database.persistRecord(recordTomorrow);
        assertTrue(recordTomorrow.getDate().equals(database.getLastRecord().getDate()));
    }

    public void testIncreaseAndPersist() throws Exception {
        WorkDayRecord recordYesterday = new WorkDayRecord(new Date(new Date().getTime() - 1000*60*60*24)).increaseTime(100000);
        database.persistRecord(recordYesterday);
        WorkDayRecord record = database.persistRecord(database.getLastRecordTodayOrCreateNew().increaseTime(10000));

        assertEquals(record.getTimeOut(), database.getLastRecord().getTimeOut());

    }

    public void testGetAllRecords(){
        WorkDayRecord recordYesterday = new WorkDayRecord(new Date(new Date().getTime() - 1000*60*60*24)).increaseTime(100000);
        WorkDayRecord recordToday = new WorkDayRecord().increaseTime(100000);
        WorkDayRecord recordTomorrow = new WorkDayRecord(new Date(new Date().getTime() + 1000*60*60*24)).increaseTime(100000);

        database.persistRecord(recordYesterday);
        database.persistRecord(recordToday);
        database.persistRecordWithoutCheck(recordTomorrow);

        ArrayList<WorkDayRecord> records = (ArrayList) database.getAllRecords();

        assertEquals(records.get(0).getDate(), recordYesterday.getDate());
        assertEquals(records.get(1).getDate(), recordToday.getDate());
        assertEquals(records.get(2).getDate(), recordTomorrow.getDate());

    }


    public void testGetThisWeekRecords(){
        long hour = 1000*60*60;
        long twentyFiveHours = 25 * hour;

        WorkDayRecord recordNow = new WorkDayRecord();

        Date date = new Date(new Date().getTime() + twentyFiveHours);
        WorkDayRecord recordTomorrow = new WorkDayRecord(date);

        date = new Date(new Date().getTime() - 24 * hour);
        WorkDayRecord recordYesterday = new WorkDayRecord(date);

        date = new Date(new Date().getTime() - 7 * 24 * hour);
        WorkDayRecord recordAWeekAgo = new WorkDayRecord(date);

        date = new Date(new Date().getTime() - 31 * 24 * hour);
        WorkDayRecord recordAMonthAgo = new WorkDayRecord(date);

        database.persistRecord(recordAMonthAgo);
        database.persistRecord(recordAWeekAgo);
        database.persistRecord(recordYesterday);
        database.persistRecord(recordNow);
        database.persistRecord(recordTomorrow);

        for(WorkDayRecord r:database.getThisWeekRecords())
            Log.d("===================== \t", r.toString());

    }

    public void testReplaceCurrentDayRecords(){

    }
}
