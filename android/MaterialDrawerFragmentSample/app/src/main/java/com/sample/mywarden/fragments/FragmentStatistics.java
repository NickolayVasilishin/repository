package com.sample.mywarden.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.mywarden.R;


public class FragmentStatistics extends Fragment {
    public final static String NAVI_DRAW_ITEM_NAME = "Statistics";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }
}
