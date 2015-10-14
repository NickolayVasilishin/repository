package com.catwithbat.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.catwithbat.mywarden.MainActivity;
import com.catwithbat.mywarden.R;
import com.catwithbat.mywarden.wardenutils.TimeWarden;
import com.catwithbat.mywarden.wardenutils.database.WardenDataBase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.rey.material.widget.Button;

import java.util.ArrayList;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class FragmentWorkDay extends Fragment implements View.OnClickListener {
    public static String TAG = "Work Day";

    private WardenDataBase database;
    private TimeWarden timeWarden;
    private View view;

    private PieChart mPieChart;
    private Button mStartWorkButton;

    public TimeWarden init(View view){
        return new TimeWarden(getContext(), view, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workday, container, false);
        FragmentUtils.setToolbarTitle(getActivity(), TAG);
        timeWarden = init(view);
        database = new WardenDataBase(getContext(), WardenDataBase.DATABASE_NAME, null, WardenDataBase.DATABASE_VERSION);

        mPieChart = setUpPieChart(view, 1);
        mStartWorkButton = (Button) view.findViewById(R.id.workDayStartButton);
        mStartWorkButton.setOnClickListener(this);
        return view;
    }


    //TODO check lifecycle of activity. Crashes when activity is on pause.
    @Override
    public void onResume(){
        if(((MainActivity)getActivity()).isWorking()) {
            timeWarden.start();
            mStartWorkButton.setText("STOP WORKING");
        } else
            mStartWorkButton.setText("START WORKING");
        setUpPieChart(getView(), 1);
        super.onResume();
    }

    private PieChart setUpPieChart(View view, int count){
        PieChart chart = (PieChart) view.findViewById(R.id.workDayChart);
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
//        chart.setHoleColor(getResources().getColor(R.color.chart_value_other));

        chart.setCenterTextSize(45);
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
        yVals1.add(new Entry(database.getTotalTimePerWeek(), 0));
        yVals1.add(new Entry((float) 1000* 60 * 60 * 30 - database.getTotalTimePerWeek(), 1));
        Log.d(this.getClass().toString(), "===== total time: " + database.getTotalTimePerWeek());
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

        chart.animateY(1000, Easing.EasingOption.EaseOutCubic);
//        chart.invalidate();

        return chart;
    }

    @Override
    public void onClick(View v) {
        if(((MainActivity)getActivity()).isWorking()) {
            timeWarden.interrupt();
            timeWarden = init(view);
            Toast.makeText(getContext(), "Work is finished", Toast.LENGTH_SHORT).show();
            mStartWorkButton.setText("START WORKING");
        } else {
            timeWarden.start();
            Toast.makeText(getContext(), "Work is started", Toast.LENGTH_SHORT).show();
            mStartWorkButton.setText("STOP WORKING");
        }
        ((MainActivity)getActivity()).setWorking(!((MainActivity)getActivity()).isWorking());
    }

    @Override
    public void onPause(){
        timeWarden.interrupt();
        super.onPause();
    }
}
