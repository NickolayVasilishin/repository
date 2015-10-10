package com.catwithbat.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.catwithbat.mywarden.R;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class FragmentWorkDay extends Fragment {
    public static String TAG = "WorkDay";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workday, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(TAG);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }
}
