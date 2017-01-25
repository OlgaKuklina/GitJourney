package com.oklab.githubjourney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.oklab.githubjourney.adapters.ContributionsListAdapter;
import com.oklab.githubjourney.githubjourney.R;

/**
 * Created by olgakuklina on 2017-01-24.
 */

public class MainViewFragment extends Fragment {

    private static final String TAG = MainViewFragment.class.getSimpleName();
    private ContributionsListAdapter contributionsListAdapter;
    private GridView gridView;

    public MainViewFragment() {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contributionsListAdapter = new ContributionsListAdapter(this.getActivity());
        gridView.setAdapter(contributionsListAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) v.findViewById(R.id.gridview);

        return v;

    }
}