package com.sample.mywarden.wardenutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by n.vasilishin on 05.10.2015.
 */
public class DataBaseWarden extends SQLiteOpenHelper implements BaseColumns {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SQLiteDatabase database;


    public static final String DATABASE_NAME = "mywardendatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_TIME_TABLE = "time_records";
    public static final String DB_T_DATE_COLUMN = "date";
    public static final String DB_T_TIME_IN_COLUMN = "time_in";
    public static final String DB_T_TIME_OUT_COLUMN = "time_out";
    public static final String DB_T_HOURS_COLUMN = "hours";

    public static final String DATABASE_TIME_TOTAL_TABLE = "time_total";
    public static final String DB_TT_TIME_TOTAL_COLUMN = "time_total";


    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TIME_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + DB_T_DATE_COLUMN + " text not null, "
            + DB_T_TIME_IN_COLUMN + " text not null, "
            + DB_T_TIME_OUT_COLUMN + " text not null, "
            + DB_T_HOURS_COLUMN + " integer);";
    private static final String DATABASE_CREATE_TIMER_SCRIPT = "create table "
            + DATABASE_TIME_TOTAL_TABLE + " ("
            + DB_TT_TIME_TOTAL_COLUMN + " integer);";

    public DataBaseWarden(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.e("OPENED", "DATABASE IS OPENING");
       // db.execSQL("DROP TABLE " + DATABASE_TIME_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("ON CREATE", "DATABASE IS CREATING");
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT);
        sqLiteDatabase.execSQL(DATABASE_CREATE_TIMER_SCRIPT);
        sqLiteDatabase.replace(DATABASE_TIME_TOTAL_TABLE, null, TimeWarden.parseValues(new String[]{DATABASE_TIME_TOTAL_TABLE, 1000 * 60 * 6 * 30 + ""}));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + i + " на версию " + i1);

        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TIME_TABLE);
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }
}
