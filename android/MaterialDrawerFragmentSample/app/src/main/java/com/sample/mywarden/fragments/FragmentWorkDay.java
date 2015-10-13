package com.sample.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.pkmmte.view.CircularImageView;
import com.sample.mywarden.R;
import com.sample.mywarden.wardenutils.DataBaseWarden;
import com.sample.mywarden.wardenutils.TimeWarden;

import java.util.ArrayList;


public class FragmentWorkDay extends Fragment {
    private TimeWarden timeWarden;
    private static long TOTAL_TIME = 1000*60*60*30;


    private final static String salaryText = "Current salary is ";
    private static int count = 0;
    private Thread runner;
    private PieChart mChart;
    private Button mCheckInButton;
    private TextView mCurrentSalary;
    private CircularImageView mPieCenter;
    private CircularImageView mPieBorder;
    private TextView mTotalTime;
    private int salary = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workday, container, false);
        mChart = setUpPieChart(view, 1);

        mTotalTime = (TextView)view.findViewById(R.id.total_time);
        mPieBorder = (CircularImageView)view.findViewById(R.id.piechart_shadow);
        mPieCenter = (CircularImageView)view.findViewById(R.id.piechart_center);
//        mPieCenter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeChart(v);
//            }
//        });

        timeWarden = new TimeWarden(new DataBaseWarden(getActivity(), DataBaseWarden.DATABASE_NAME, null, 1).getWritableDatabase());


        mCurrentSalary = (TextView) view.findViewById(R.id.currentSalaryTextView);
        mCurrentSalary.setText(salaryText + salary++ + "$");

        mCheckInButton = (Button) view.findViewById(R.id.checkInWorkButton);
        mCheckInButton.setText("CHECK IN");
        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckInButton.getText().equals("CHECK IN")) {
                    mCheckInButton.setText("CHECK OUT");
                    Toast.makeText(getContext(), "Service is disabled due to database problems", Toast.LENGTH_SHORT).show();
                    //getActivity().startService(new Intent(getActivity(), TimeWarden.class));
                    countAndPrintHours();
                }
                else {
                   //getActivity().stopService(new Intent(getActivity(), TimeWarden.class));
                    runner.interrupt();
                    mCheckInButton.setText("CHECK IN");
                }
                mCurrentSalary.setText(salaryText + salary++ + "$");

            }
        });

        mCheckInButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mCheckInButton.setBackgroundColor(getResources().getColor(R.color.ap_button_default));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCheckInButton.setBackgroundColor(getResources().getColor(R.color.ap_button_pressed));
                }
                return false;
            }
        });

        return view;

    }

    private void changeChart(View view) {
        if(view.findViewById(R.id.workDayChart)==null)
            Toast.makeText(view.getContext(), "Found view", Toast.LENGTH_SHORT).show();
    }


    private PieChart setUpPieChart(View view, int count){
        PieChart chart = (PieChart) view.findViewById(R.id.workDayChart);
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
//        chart.setHoleColor(getResources().getColor(R.color.chart_value_other));


        chart.setHoleRadius(50f);
//        chart.setTransparentCircleColor(Color.BLACK);
//        chart.setTransparentCircleAlpha(110);
        chart.setTransparentCircleRadius(50f);
        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);

        ArrayList<Entry> yVals1 = new ArrayList<>();
        //TODO time as variable
        yVals1.add(new Entry((float) 0, 0));
        yVals1.add(new Entry((float) 60 * 60 * 30, 1));
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < count + 1; i++)
            xVals.add("" + i);
        PieDataSet dataSet = new PieDataSet(yVals1, "Work Time");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.chart_value_best));
        colors.add(getResources().getColor(R.color.chart_value_other));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
//        chart.highlightValues(null);
        chart.getLegend().setEnabled(false);
        chart.getData().setDrawValues(false);
        chart.setDrawSliceText(false);
        chart.animateY(1000, Easing.EasingOption.EaseInQuad);
        chart.invalidate();

        return chart;
    }

    private void addEntry(float val){
        PieData data = mChart.getData();
        if (data != null) {
            data.getDataSetByLabel("Work Time", true).getEntryForXIndex(0).setVal(data.getDataSetByLabel("Work Time", true).getEntryForXIndex(0).getVal()+val);
            data.getDataSetByLabel("Work Time", true).getEntryForXIndex(1).setVal(data.getDataSetByLabel("Work Time", true).getEntryForXIndex(1).getVal()-val);
            mChart.notifyDataSetChanged();
            mChart.animate();
            mChart.invalidate();
        }
    }

    private void countAndPrintHours() {
       runner = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    //timeWarden.getLastRecord().getHours();
                    //TODO Pool of threads. Too expensive to create a thread for view change
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //float time = (TOTAL_TIME - timeWarden.getTotalTimeMillis())/1000;
                            float time = mChart.getData().getDataSetByLabel("Work Time", true).getEntryForXIndex(0).getVal()*1000;
                            if((long)time%10 == 0)
                                mTotalTime.setText("" + count++);
                            addEntry(10);

                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
        });
        runner.start();
    }

}
