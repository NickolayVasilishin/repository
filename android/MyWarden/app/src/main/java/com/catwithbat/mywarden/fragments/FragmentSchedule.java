package com.catwithbat.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catwithbat.mywarden.R;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class FragmentSchedule extends Fragment {
    public static String TAG = "Schedule";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        FragmentUtils.setToolbarTitle(getActivity(), TAG);
        return view;
    }


}