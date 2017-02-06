package com.oklab.githubjourney.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oklab.githubjourney.adapters.ContributionsListAdapter;
import com.oklab.githubjourney.githubjourney.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-01-24.
 */

public class MainViewFragment extends Fragment {

    private static final String TAG = MainViewFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    String monthName;
    private ContributionsListAdapter contributionsListAdapter;
    private GridView gridView;
    private LinearLayout contributionsList;
    private ScrollView scrollView;
    private Calendar calendar = (Calendar) Calendar.getInstance().clone();
    private TextView monthTitle;


    public MainViewFragment() {
        setRetainInstance(true);
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
        fragment.adjustCalendar();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contributionsListAdapter = new ContributionsListAdapter(this.getActivity(), getArguments().getInt(ARG_SECTION_NUMBER));
        gridView.setAdapter(contributionsListAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) v.findViewById(R.id.gridview);
        scrollView = (ScrollView) v.findViewById(R.id.contributions_activity_container);
        contributionsList = (LinearLayout) v.findViewById(R.id.contribution_activity);
        monthTitle = (TextView) v.findViewById(R.id.month_title);
        monthTitle.setText(monthName);
        return v;
    }

    private void adjustCalendar() {
        int offset = getArguments().getInt(ARG_SECTION_NUMBER);
        calendar.add(Calendar.MONTH, -offset);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM, yyyy");
        monthName = month_date.format(calendar.getTime());
    }
}