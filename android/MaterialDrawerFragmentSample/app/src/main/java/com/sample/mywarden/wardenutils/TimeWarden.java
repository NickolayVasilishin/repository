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
    private boolean ableToWork;
    private DataBaseWarden database;
    private SQLiteDatabase mSqLiteDatabase;

    public TimeWarden() {}

    public TimeWarden(SQLiteDatabase mSqLiteDatabase){
        this.mSqLiteDatabase = mSqLiteDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ableToWork = true;
        //database = getDatabasePath("");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mSqLiteDatabase = (SQLiteDatabase) intent.getExtras().get("sql");
        Toast.makeText(this, "Service was created", Toast.LENGTH_LONG);
        while(ableToWork){
            Date timeStart = new Date();
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            increaseTime(new Date().getTime() - timeStart.getTime());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service was destroyed", Toast.LENGTH_LONG);
        super.onDestroy();
    }

    public void increaseTime(long time){
        replaceValue(getLastRecord().increaseTime(time));
    }

    public ContentValues parseValues(String ... values){
        ContentValues newValues = new ContentValues();
        for(int i = 0; i < values.length; i += 2)
            newValues.put(values[i], values[i+1]);
        return newValues;
    }

    private void replaceValue(WorkDayRecord workDayRecord) {
        replaceValue(DataBaseWarden.DATABASE_TIME_TABLE, workDayRecord.getDate(), workDayRecord.getTimeIn(), workDayRecord.getTimeOut(), ""+workDayRecord.getHours());
    }

    public void replaceValue(String tableName, String ... values){
        mSqLiteDatabase.replace(tableName, null, parseValues(values));
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
        return new WorkDayRecord(cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_DATE_COLUMN)), cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_TIME_IN_COLUMN)), cursor.getString(cursor.getColumnIndex(DataBaseWarden.DB_T_TIME_OUT_COLUMN)), cursor.getInt(cursor.getColumnIndex(DataBaseWarden.DB_T_HOURS_COLUMN)));
    }
}
