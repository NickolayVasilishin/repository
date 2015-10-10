package com.catwithbat.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catwithbat.mywarden.R;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class FragmentStatistics extends Fragment {
    public static String TAG = "Statistics";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(TAG);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }
}

