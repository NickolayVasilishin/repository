package com.sample.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.sample.mywarden.R;
import com.sample.mywarden.wardenutils.DataBaseWarden;
import com.sample.mywarden.wardenutils.TimeWarden;

import java.util.ArrayList;


public class FragmentWorkDay extends Fragment {
    private TimeWarden timeWarden;


    private final static String salaryText = "Current salary is ";
    private static int count = 1;
    private Thread runner;
    private PieChart mChart;
    private Button mCheckInButton;
    private TextView mCurrentSalary;
    private int salary = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workday, container, false);
        mChart = setUpPieChart(view, 1);
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

                   // getActivity().startService(new Intent(getActivity(), TimeWarden.class).putExtra("sql", new DataBaseWarden(getActivity(), DataBaseWarden.DATABASE_NAME, null, 1).getWritableDatabase()));
                    feedMultiple();
                }
                else {
                   // getActivity().stopService(new Intent(getActivity(), TimeWarden.class));
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
        chart.setCenterText("15h");
        chart.setCenterTextSize(50f);
        chart.setCenterTextColor(getResources().getColor(R.color.ap_text_bright));
        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < count + 1; i++)
            yVals1.add(new Entry((float) 60*60*15, i));
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < count + 1; i++)
            xVals.add("" + i);
        PieDataSet dataSet = new PieDataSet(yVals1, "Work Time");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

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
        chart.setCenterTextWordWrapEnabled(true);
        chart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        chart.invalidate();

        return chart;
    }

    private void addEntry(){
        PieData data = mChart.getData();
        if (data != null) {
            data.getDataSetByLabel("Work Time", true).getEntryForXIndex(0).setVal(data.getDataSetByLabel("Work Time", true).getEntryForXIndex(0).getVal()+1);
            data.getDataSetByLabel("Work Time", true).getEntryForXIndex(1).setVal(data.getDataSetByLabel("Work Time", true).getEntryForXIndex(1).getVal()-1);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void feedMultiple() {
       runner = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    try {
                        Thread.sleep(10);
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
