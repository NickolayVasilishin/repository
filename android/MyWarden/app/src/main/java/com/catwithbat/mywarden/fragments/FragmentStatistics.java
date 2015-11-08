package com.catwithbat.mywarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.catwithbat.mywarden.R;
import com.catwithbat.mywarden.wardenutils.WorkDayRecord;
import com.catwithbat.mywarden.wardenutils.database.local.WardenDatabaseLocal;

/**
 * Created by n.vasilishin on 10.10.2015.
 */
public class FragmentStatistics extends Fragment {
    public static String TAG = "Statistics";
    public ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        FragmentUtils.setToolbarTitle(getActivity(), TAG);
        listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<WorkDayRecord> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new WardenDatabaseLocal(getContext(), WardenDatabaseLocal.DATABASE_NAME, null, WardenDatabaseLocal.DATABASE_VERSION).getAllRecords());
        listView.setAdapter(arrayAdapter);
        return view;
    }
}

