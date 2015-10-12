package com.catwithbat.mywarden.wardenutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.catwithbat.mywarden.wardenutils.database.WardenDataBase;

import java.util.Date;

/**
 * Created by n.vasilishin on 12.10.2015.
 */
public class WardenTimeService extends Service {
    private static long MINUTE = 1000*60;
    private static long HOUR = MINUTE * 60;

    private WardenDataBase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new WardenDataBase(this, WardenDataBase.DATABASE_NAME, null, WardenDataBase.DATABASE_VERSION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            long time = new Date().getTime();
            Thread.sleep(1000);
            long delta = new Date().getTime() - time;
            increaseTime(delta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void increaseTime(long time){
        database.persistRecord(database.getLastRecordTodayOrCreateNew().increaseTime(time));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
