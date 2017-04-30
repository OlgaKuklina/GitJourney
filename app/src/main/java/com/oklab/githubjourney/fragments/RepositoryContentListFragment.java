package com.oklab.githubjourney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.adapters.RepoContentListAdapter;
import com.oklab.githubjourney.asynctasks.RepoContentLoader;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;

import java.util.List;

/**
 * Created by olgakuklina on 2017-04-26.
 */
public class RepositoryContentListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = RepositoryContentListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RepoContentListAdapter repoContentListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RepoContentFragmentInteractionListener repoContentChangedlistner;

    public RepositoryContentListFragment() {
    }

    public static RepositoryContentListFragment newInstance(RepoContentFragmentInteractionListener repoContentChangedListener, String path, String repoName, String loginName) {
        RepositoryContentListFragment fragment = new RepositoryContentListFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        args.putString("repoName", repoName);
        args.putString("login", loginName);
        fragment.setArguments(args);
        fragment.repoContentChangedlistner = repoContentChangedListener;
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
        View v = inflater.inflate(R.layout.fragment_repository_content, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.repo_content_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.repo_content_swipe_refresh_layout);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        repoContentListAdapter = new RepoContentListAdapter(this.getContext());
        recyclerView.setAdapter(repoContentListAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        getLoaderManager().initLoader(0, getArguments(), new RepositoryContentListFragment.RepoContentLoaderCallbacks());
    }

    @Override
    public void onRefresh() {
    }

    private class RepoContentLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<RepositoryContentDataEntry>> {

        @Override
        public Loader<List<RepositoryContentDataEntry>> onCreateLoader(int id, Bundle args) {
            return new RepoContentLoader(getContext(), args.getString("path"), args.getString("repoName"), args.getString("login"));
        }

        @Override
        public void onLoadFinished(Loader<List<RepositoryContentDataEntry>> loader, List<RepositoryContentDataEntry> repoContentDataEntryList) {
            Log.v(TAG, "onLoadFinished " + repoContentDataEntryList);
            if (repoContentDataEntryList != null && !repoContentDataEntryList.isEmpty()) {
                repoContentListAdapter.add(repoContentDataEntryList);
            }
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<RepositoryContentDataEntry>> loader) {
        }
    }
    public interface RepoContentFragmentInteractionListener {
        void onPathChanged(String newPath);
    }
}
