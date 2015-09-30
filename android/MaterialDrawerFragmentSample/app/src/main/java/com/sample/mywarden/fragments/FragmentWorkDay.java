package com.sample.mywarden.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sample.mywarden.R;

import java.util.ArrayList;
import java.util.Random;


public class FragmentWorkDay extends Fragment {
    public final static String NAVI_DRAW_ITEM_NAME = "Work Day";
    private final static String salaryText = "Current salary is ";

    private LineChart mChart;
    private Button mCheckInButton;
    private TextView mCurrentSalary;
    private int salary = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workday, container, false);
        mChart = (LineChart) view.findViewById(R.id.workDayChart);
        mCurrentSalary = (TextView) view.findViewById(R.id.currentSalaryTextView);
        mCurrentSalary.setText(salaryText + salary++ + "$");
        mCheckInButton = (Button) view.findViewById(R.id.checkInWorkButton);
        mCheckInButton.setText("Check in");
        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckInButton.getText().equals("Check in"))
                    mCheckInButton.setText("Check out");
                else
                    mCheckInButton.setText("Check in");
                mCurrentSalary.setText(salaryText + salary++ + "$");
                mChart.setData(createData());
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        });

        mChart.setData(createData());
        mChart.invalidate();
        return view;

    }

    private LineData createData(){
        LineDataSet set = new LineDataSet(createRandomValues(), "Values");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return new LineData(new String[] {"1","2","3","4","5","6","7","8","9","10"}, set);
    }

    private ArrayList<Entry> createRandomValues(){
        ArrayList<Entry> vals = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < 10; i++)
            vals.add(new Entry(r.nextFloat()*10, i));
        return vals;
    }

}
