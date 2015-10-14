package com.catwithbat.mywarden.wardenutils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.catwithbat.mywarden.R;
import com.catwithbat.mywarden.wardenutils.database.WardenDataBase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

/**
 * Created by n.vasilishin on 12.10.2015.
 */
public class TimeWarden extends Thread {

    private View view;
    private Activity activity;
    private WardenDataBase database;
    private static long rate = 5000;

    private UiThread uiWorker;

    public TimeWarden(Context context, View view, Activity activity) {
        database = new WardenDataBase(context, WardenDataBase.DATABASE_NAME, null, WardenDataBase.DATABASE_VERSION);
        this.view = view;
        uiWorker = new UiThread();
        this.activity = activity;
    }

    private WorkDayRecord increaseTime(){
        return database.persistRecord(database.getLastRecordTodayOrCreateNew().increaseTime(new WorkDayRecord()));
    }

    @Override
    public void run() {
        Log.d("UiThread", "======= Starting TimeWarden Thread");
        uiWorker.start();
        while(!isInterrupted()){
            Log.d("UiThread", "======= Entering Cycle");
            try {
                synchronized (uiWorker){
                    uiWorker.setRecord(increaseTime());
                    uiWorker.notify();
                }
                Thread.sleep(rate);
            } catch (InterruptedException e) {
                e.printStackTrace();
                uiWorker.interrupt();
                interrupt();
            }
        }
    }




    private class UiThread extends Thread{
        private WorkDayRecord record;

        @Override
        public void run(){
            final PieChart mChart = (PieChart) view.findViewById(R.id.workDayChart);
            while(!isInterrupted()){
                synchronized (this) {
                    try {
                        this.wait();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addEntry(mChart, record.getTime());
                                mChart.setCenterText(record.getHoursAsString());
                            }
                        });

                        Log.d("UiThread", "=======" + record.getTime());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        interrupt();
                    }
                }
            }
        }

        private void addEntry(PieChart mChart, float val){
            Log.d("UiThread", "======= Adding Entry" + record.getTime());
            PieData data = mChart.getData();
            if (data != null) {
                data.getDataSetByLabel("Work Time", true).getEntryForXIndex(0).setVal(data.getDataSetByLabel("Work Time", true).getEntryForXIndex(0).getVal()+val);
                data.getDataSetByLabel("Work Time", true).getEntryForXIndex(1).setVal(data.getDataSetByLabel("Work Time", true).getEntryForXIndex(1).getVal()-val);
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }


        public Easing.EasingOption getAnim(int i){
            return Easing.EasingOption.values()[i];
        }

        private void setRecord(WorkDayRecord r){
            record = r;
        }
    }









}
