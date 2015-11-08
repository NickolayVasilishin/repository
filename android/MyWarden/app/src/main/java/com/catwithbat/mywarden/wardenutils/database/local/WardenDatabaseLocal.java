package com.catwithbat.mywarden.wardenutils.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.catwithbat.mywarden.wardenutils.WorkDayRecord;
import com.catwithbat.mywarden.wardenutils.database.WardenDatabaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class WardenDatabaseLocal extends SQLiteOpenHelper implements BaseColumns, WardenDatabaseService {

    private SQLiteDatabase database;

    public static final String DATABASE_NAME = "mywardendatabase.db";
    public static final int DATABASE_VERSION = 101313;

    public static final String DATABASE_TIME_TABLE = "time_records";
    public static final String DB_T_DATE_COLUMN = "date";
    public static final String DB_T_TIME_IN_COLUMN = "time_in";
    public static final String DB_T_TIME_OUT_COLUMN = "time_out";
    public static final String DB_T_TIME_COLUMN = "time";


    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TIME_TABLE + " ("
            + DB_T_DATE_COLUMN + " text primary key not null, "
            + DB_T_TIME_IN_COLUMN + " text not null, "
            + DB_T_TIME_OUT_COLUMN + " text not null, "
            + DB_T_TIME_COLUMN + " integer"
            + ")";


    public WardenDatabaseLocal(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        database = getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("ON CREATE", "DATABASE IS CREATING");
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + DATABASE_TIME_TABLE);
        db.execSQL("DROP TABLE IF EXISTS time_total");
        onCreate(db);
    }

    public static ContentValues parseValues(WorkDayRecord record){
        String date = record.getDate();
        String timeIn = record.getTimeIn();
        String timeOut = record.getTimeOut();
        String time = "" + record.getTime();

        ContentValues values = parseValues(
                new String[]{
                        DB_T_DATE_COLUMN, date,
                        DB_T_TIME_IN_COLUMN, timeIn,
                        DB_T_TIME_OUT_COLUMN, timeOut,
                        DB_T_TIME_COLUMN, time,
                });

        return values;
    }

    public static ContentValues parseValues(String ... values){
        ContentValues newValues = new ContentValues();
        for(int i = 0; i < values.length; i += 2)
            newValues.put(values[i], values[i+1]);
        return newValues;
    }

    public WorkDayRecord persistRecord(WorkDayRecord record){
        ContentValues values = parseValues(record);

        try {
            if(getLastRecord().isToday())
                database.update(WardenDatabaseLocal.DATABASE_TIME_TABLE, values, WardenDatabaseLocal.DB_T_DATE_COLUMN + " = ?", new String[] { getLastRecord().getDate()});
            else
                database.insert(DATABASE_TIME_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            database.insert(DATABASE_TIME_TABLE, null, values);
        }

        return record;
    }

    public WorkDayRecord persistRecordWithoutCheck(WorkDayRecord record){
        database.insert(DATABASE_TIME_TABLE, null, parseValues(record));
        return record;
    }

    public WorkDayRecord getLastRecordTodayOrCreateNew(){
        WorkDayRecord record = null;
        try {
            record = getLastRecord();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(this.getClass().toString(), "");
        }
        return record != null && record.isToday() ? record : new WorkDayRecord();
    }

    public WorkDayRecord getLastRecord() throws Exception{
        Cursor cursor = database.query(WardenDatabaseLocal.DATABASE_TIME_TABLE, new String[]{WardenDatabaseLocal.DB_T_DATE_COLUMN,
                        WardenDatabaseLocal.DB_T_TIME_IN_COLUMN, WardenDatabaseLocal.DB_T_TIME_OUT_COLUMN, WardenDatabaseLocal.DB_T_TIME_COLUMN},
                null, null,
                null, null, null);
        cursor.moveToLast();

        WorkDayRecord record = new WorkDayRecord(
                cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_DATE_COLUMN)),
                cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_IN_COLUMN)),
                cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_OUT_COLUMN)),
                cursor.getInt(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_COLUMN))
        );
        cursor.close();
        return record;
    }

    public List<WorkDayRecord> getAllRecords(){
        List<WorkDayRecord> records = new ArrayList<>();

        Cursor cursor = database.query(WardenDatabaseLocal.DATABASE_TIME_TABLE, new String[]{WardenDatabaseLocal.DB_T_DATE_COLUMN,
                        WardenDatabaseLocal.DB_T_TIME_IN_COLUMN, WardenDatabaseLocal.DB_T_TIME_OUT_COLUMN, WardenDatabaseLocal.DB_T_TIME_COLUMN},
                null, null,
                null, null, null);
        if(cursor.moveToFirst()) {
            do {
                records.add(new WorkDayRecord(
                        cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_DATE_COLUMN)),
                        cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_IN_COLUMN)),
                        cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_OUT_COLUMN)),
                        cursor.getInt(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_COLUMN))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public float getTotalTimePerWeek(){
        float time = 0f;
        for(WorkDayRecord record:getThisWeekRecords())
            time += record.getTime();

        return time;
    }

    @Deprecated
    public float getTotalHoursPerWeek(){
        float hours = 0f;
        for(WorkDayRecord record:getThisWeekRecords())
            hours += record.getTime();

        return hours;
    }

    public List<WorkDayRecord> getThisWeekRecords(){
        List<WorkDayRecord> records = new ArrayList<>();
        Cursor cursor = database.query(WardenDatabaseLocal.DATABASE_TIME_TABLE, new String[]{WardenDatabaseLocal.DB_T_DATE_COLUMN,
                        WardenDatabaseLocal.DB_T_TIME_IN_COLUMN, WardenDatabaseLocal.DB_T_TIME_OUT_COLUMN, WardenDatabaseLocal.DB_T_TIME_COLUMN},
                null, null,
                null, null, null);
        if(cursor.moveToLast()){
            do {
                WorkDayRecord record = new WorkDayRecord(
                        cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_DATE_COLUMN)),
                        cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_IN_COLUMN)),
                        cursor.getString(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_OUT_COLUMN)),
                        cursor.getInt(cursor.getColumnIndex(WardenDatabaseLocal.DB_T_TIME_COLUMN))
                );

                if(!record.isCurrentWeek())
                    break;

                records.add(record);
            } while (cursor.moveToPrevious());
        }
        return reverse(records);
    }

    public static List reverse(List reversedRecords){
        int index = reversedRecords.size()-1;
        List records = new ArrayList<>();
        for(; index >= 0;)
            records.add(reversedRecords.get(index--));
        return records;
    }
}
