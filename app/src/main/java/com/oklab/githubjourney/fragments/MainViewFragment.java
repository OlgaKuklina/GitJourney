package com.oklab.githubjourney.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.oklab.githubjourney.activities.GeneralActivity;
import com.oklab.githubjourney.adapters.ContributionsListAdapter;
import com.oklab.githubjourney.githubjourney.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-01-24.
 */

public class MainViewFragment extends Fragment {

    private static final String TAG = MainViewFragment.class.getSimpleName();
    private ContributionsListAdapter contributionsListAdapter;
    private GridView gridView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    String monthName;
    private Calendar calendar = Calendar.getInstance();
    private TextView monthTitle;


    public MainViewFragment() {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contributionsListAdapter = new ContributionsListAdapter(this.getActivity());
        gridView.setAdapter(contributionsListAdapter);

    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainViewFragment newInstance(int sectionNumber) {
        MainViewFragment fragment = new MainViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) v.findViewById(R.id.gridview);
        monthTitle = (TextView) v.findViewById(R.id.month_title);

        int offset = getArguments().getInt(ARG_SECTION_NUMBER);
        calendar.add(Calendar.MONTH, -offset);

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        monthName = month_date.format(calendar.getTime());
        monthTitle.setText(monthName);
        return v;

    }
}