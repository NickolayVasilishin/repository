package com.catwithbat.mywarden.wardenutils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.catwithbat.mywarden.wardenutils.WorkDayRecord;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class WardenDataBase extends SQLiteOpenHelper implements BaseColumns {

    private SQLiteDatabase database;

    public static final String DATABASE_NAME = "mywardendatabase.db";
    public static final int DATABASE_VERSION = 10;

    public static final String DATABASE_TIME_TABLE = "time_records";
    public static final String DB_T_DATE_COLUMN = "date";
    public static final String DB_T_TIME_IN_COLUMN = "time_in";
    public static final String DB_T_TIME_OUT_COLUMN = "time_out";
    public static final String DB_T_HOURS_COLUMN = "hours";

    public static final String DATABASE_TIME_TOTAL_TABLE = "time_total";
    public static final String DB_TT_TIME_TOTAL_COLUMN = "time";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TIME_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + DB_T_DATE_COLUMN + " text not null, "
            + DB_T_TIME_IN_COLUMN + " text not null, "
            + DB_T_TIME_OUT_COLUMN + " text not null, "
            + DB_T_HOURS_COLUMN + " integer);";
    private static final String DATABASE_CREATE_TIMER_SCRIPT = "create table "
            + DATABASE_TIME_TOTAL_TABLE + " ("
            + DB_TT_TIME_TOTAL_COLUMN + " integer);";


    public WardenDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        database = getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("ON CREATE", "DATABASE IS CREATING");
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT);
        sqLiteDatabase.execSQL(DATABASE_CREATE_TIMER_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + DATABASE_TIME_TABLE);
        onCreate(db);
    }

    public static ContentValues parseValues(WorkDayRecord record){
        String date = record.getDate();
        String timeIn = record.getTimeIn();
        String timeOut = record.getTimeOut();
        String hours = record.getHours();

        ContentValues values = parseValues(
                new String[]{
                        DB_T_DATE_COLUMN, date,
                        DB_T_TIME_IN_COLUMN, timeIn,
                        DB_T_TIME_OUT_COLUMN, timeOut,
                        DB_T_HOURS_COLUMN, hours
                });

        return values;
    }

    public static ContentValues parseValues(String ... values){
        ContentValues newValues = new ContentValues();
        for(int i = 0; i <= values.length; i += 2)
            newValues.put(values[i], values[i+1]);
        return newValues;
    }

    public WorkDayRecord persistRecord(WorkDayRecord record){
        String date = record.getDate();
        String timeIn = record.getTimeIn();
        String timeOut = record.getTimeOut();
        String hours = record.getHours();

        ContentValues values = parseValues(
                new String[] {
                                DB_T_DATE_COLUMN, date,
                                DB_T_TIME_IN_COLUMN, timeIn,
                                DB_T_TIME_OUT_COLUMN, timeOut,
                                DB_T_HOURS_COLUMN, hours
        });

        try {
            if(getLastRecord().isToday())
                database.replace(DATABASE_TIME_TABLE, null, values);
            else
                database.insert(DATABASE_TIME_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            database.insert(DATABASE_TIME_TABLE, null, values);
        }

        return record;
    }

    public WorkDayRecord getLastRecordOrCreateNew(){
        WorkDayRecord record = null;
        try {
            record = getLastRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record != null && record.isToday() ? record : new WorkDayRecord();
    }

    public WorkDayRecord getLastRecord() throws Exception{
        Cursor cursor = database.query(WardenDataBase.DATABASE_TIME_TABLE, new String[]{WardenDataBase.DB_T_DATE_COLUMN,
                        WardenDataBase.DB_T_TIME_IN_COLUMN, WardenDataBase.DB_T_TIME_OUT_COLUMN, WardenDataBase.DB_T_HOURS_COLUMN},
                null, null,
                null, null, null) ;
        cursor.moveToLast();

        WorkDayRecord record = new WorkDayRecord(cursor.getString(cursor.getColumnIndex(WardenDataBase.DB_T_DATE_COLUMN)), cursor.getString(cursor.getColumnIndex(WardenDataBase.DB_T_TIME_IN_COLUMN)), cursor.getString(cursor.getColumnIndex(WardenDataBase.DB_T_TIME_OUT_COLUMN)), cursor.getInt(cursor.getColumnIndex(WardenDataBase.DB_T_HOURS_COLUMN)));
        return record;
    }
}
