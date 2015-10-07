package com.sample.mywarden.wardenutils;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by n.vasilishin on 05.10.2015.
 */
public class TimeWarden extends Service {
    private static long MINUTE = 1000*60;
    private static long HOUR = MINUTE * 60;

    private boolean ableToWork;
    private SQLiteDatabase mSqLiteDatabase;

    public TimeWarden() {}

    public TimeWarden(SQLiteDatabase mSqLiteDatabase){
        this.mSqLiteDatabase = mSqLiteDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ableToWork = true;
        mSqLiteDatabase = new DataBaseWarden(this, DataBaseWarden.DATABASE_NAME, null, 1).getWritableDatabase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service was created", Toast.LENGTH_LONG).show();
        while(ableToWork){
            Date timeStart = new Date();
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            long time = new Date().getTime() - timeStart.getTime();
            increaseTime(time);
            decreaseTotalTime(time);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private long decreaseTotalTime(long time) {
        long hours = getTotalTimeMillis();
        hours -= time;
        mSqLiteDatabase.replace(DataBaseWarden.DATABASE_TIME_TOTAL_TABLE, null, parseValues(new String[]{DataBaseWarden.DB_TT_TIME_TOTAL_COLUMN, "" + time}));
        return hours;
    }

    public long getTotalTimeMillis(){
        Cursor cursor = mSqLiteDatabase.query(DataBaseWarden.DATABASE_TIME_TOTAL_TABLE, new String[]{DataBaseWarden.DB_TT_TIME_TOTAL_COLUMN},
                null, null,
                null, null, null) ;
        cursor.moveToLast();
        return cursor.getLong(cursor.getColumnIndex(DataBaseWarden.DB_TT_TIME_TOTAL_COLUMN));
    }

    /**
     *  Total time to work
     * @return HH:mm
     */
    public String getTotalTime(){
        return "" + getTotalTimeMillis()/HOUR + ":" + getTotalTimeMillis()/MINUTE;
    }

    public String getTotalTimeHours(){
        return "" + getTotalTimeMillis()/HOUR;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service was destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    public void increaseTime(long time){
        replaceWorkDayRecord(getLastRecord().increaseTime(time));
    }

    public static ContentValues parseValues(String ... values){
        ContentValues newValues = new ContentValues();
        for(int i = 0; i < values.length; i += 2)
            newValues.put(values[i], values[i+1]);
        return newValues;
    }

    private void replaceWorkDayRecord(WorkDayRecord workDayRecord) {
        replaceValue(DataBaseWarden.DATABASE_TIME_TABLE, DataBaseWarden.DB_T_DATE_COLUMN, workDayRecord.getDate(), DataBaseWarden.DB_T_TIME_IN_COLUMN, workDayRecord.getTimeIn(), DataBaseWarden.DB_T_TIME_OUT_COLUMN, workDayRecord.getTimeOut(), DataBaseWarden.DB_T_HOURS_COLUMN, "" + workDayRecord.getHours());
    }

    public void replaceValue(String tableName, String ... values){
        mSqLiteDatabase.replace(tableName, null, parseValues(values));
    }

    public WorkDayRecord insertWorkDayRecord(WorkDayRecord workDayRecord) {
        insertValues(DataBaseWarden.DATABASE_NAME, DataBaseWarden.DB_T_DATE_COLUMN, workDayRecord.getDate(), DataBaseWarden.DB_T_TIME_IN_COLUMN, workDayRecord.getTimeIn(), DataBaseWarden.DB_T_TIME_OUT_COLUMN, workDayRecord.getTimeOut(), DataBaseWarden.DB_T_HOURS_COLUMN, "" + workDayRecord.getHours());
        return workDayRecord;
    }

    public void insertValues(String tableName, String ... values) throws IllegalArgumentException{
        if(values.length%2 != 0)
            throw new IllegalArgumentException();
        mSqLiteDatabase.insert(tableName, null, parseValues(values));
    }

    public WorkDayRecord getLastRecord(){
        Cursor cursor = mSqLiteDatabase.query(DataBaseWarden.DATABASE_TIME_TABLE, new String[] {DataBaseWarden.DB_T_DATE_COLUMN,
                        DataBaseWarden.DB_T_TIME_IN_COLUMN, DataBaseWarden.DB_T_TIME_OUT_COLUMN, DataBaseWarden.DB_T_HOURS_COLUMN},
                null, null,
                null, null, null) ;
        cursor.moveToLast();

        cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_DATE_COLUMN));
        WorkDayRecord record = new WorkDayRecord(cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_DATE_COLUMN)), cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_TIME_IN_COLUMN)), cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_TIME_OUT_COLUMN)), cursor.getInt(cursor.getColumnIndex(DataBaseWarden.DB_T_HOURS_COLUMN)));
        if(!record.isToday())
            return insertWorkDayRecord(new WorkDayRecord());
        return record;
    }

}
