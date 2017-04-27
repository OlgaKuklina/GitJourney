package com.oklab.githubjourney.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oklab.githubjourney.R;
import com.oklab.githubjourney.adapters.RepoContentListAdapter;

/**
 * Created by olgakuklina on 2017-04-26.
 */
public class RepositoryContentListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = RepositoryContentListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RepoContentListAdapter repoContentListAdapter;

    public RepositoryContentListFragment() {
    }

    public static RepositoryContentListFragment newInstance() {
        RepositoryContentListFragment fragment = new RepositoryContentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_general_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.repo_content_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.repo_content_swipe_refresh_layout);
        return v;
    }

    @Override
    public void onRefresh() {

    }
}
