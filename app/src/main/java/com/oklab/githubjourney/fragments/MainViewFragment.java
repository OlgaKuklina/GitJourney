package com.oklab.githubjourney.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.adapters.ContributionsListAdapter;
import com.oklab.githubjourney.data.ContributionsDataLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-01-24.
 */

public class MainViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainViewFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    String monthName;
    private ContributionsListAdapter contributionsListAdapter;
    private GridView gridView;
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
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) v.findViewById(R.id.gridview);
        scrollView = (ScrollView) v.findViewById(R.id.contributions_activity_container);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar newCalendar = (Calendar) calendar.clone();
        newCalendar.set(Calendar.DAY_OF_MONTH, 1);
        newCalendar.set(Calendar.HOUR, 0);
        newCalendar.set(Calendar.MINUTE, 0);
        newCalendar.set(Calendar.SECOND, 0);
        Log.v(TAG, "numberOfDays = " + numberOfDays);
        long minDate = newCalendar.getTimeInMillis();
        newCalendar.set(Calendar.DAY_OF_MONTH, numberOfDays);
        newCalendar.set(Calendar.HOUR, 23);
        newCalendar.set(Calendar.MINUTE, 59);
        newCalendar.set(Calendar.SECOND, 59);
        long maxDate = newCalendar.getTimeInMillis();
        return ContributionsDataLoader.newRangeLoader(getContext(), minDate, maxDate);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "loader finished " + data.getCount());
        contributionsListAdapter = new ContributionsListAdapter(this.getContext(), getArguments().getInt(ARG_SECTION_NUMBER), data);
        gridView.setAdapter(contributionsListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}