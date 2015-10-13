package com.catwithbat.mywarden.fragments;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.catwithbat.mywarden.R;


/**
 * Created by n.vasilishin on 12.10.2015.
 */
public class FragmentUtils {
    public static void setToolbarTitle(Activity activity, String text){
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(text);
        ((ActionBarActivity)activity).setSupportActionBar(toolbar);
    }

}
