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
import com.oklab.githubjourney.data.GitHubRepoContentType;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;

import java.util.List;
import java.util.Stack;

/**
 * Created by olgakuklina on 2017-04-26.
 */
public class RepositoryContentListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RepoContentListAdapter.RepoContentOnClickListener {
    private static final String TAG = RepositoryContentListFragment.class.getSimpleName();
    private static Stack<String> pathStack = new Stack<>();
    private final RepoContentLoaderCallbacks callbacks = new RepoContentLoaderCallbacks();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RepoContentListAdapter repoContentListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RepoContentFragmentInteractionListener repoContentChangedlistner;

    public RepositoryContentListFragment() {
    }

    public static RepositoryContentListFragment newInstance(RepoContentFragmentInteractionListener repoContentChangedListener, String repoName, String loginName) {
        RepositoryContentListFragment fragment = new RepositoryContentListFragment();
        Bundle args = new Bundle();
        args.putString("path", "");
        pathStack.push(args.getString("path"));
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
        repoContentListAdapter = new RepoContentListAdapter(this.getContext(), this);
        recyclerView.setAdapter(repoContentListAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        getLoaderManager().initLoader(0, getArguments(), callbacks);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onRepoItemClicked(RepositoryContentDataEntry entry) {
        repoContentChangedlistner.onPathChanged(entry.getPath());
        Bundle args = getArguments();
        switch (entry.getType()) {
            case DIR:
            case SUBMODULE:
                repoContentListAdapter.resetAllData();
                pathStack.push(args.getString("path"));
                args.putString("path", entry.getPath());
                getLoaderManager().initLoader(0, args, callbacks);
                break;
            case EMPTY:
                repoContentListAdapter.resetAllData();
                args.putString("path", pathStack.pop());
                getLoaderManager().initLoader(0, args, callbacks);
                break;
            case FILE:
                break;
            default:
                break;
        }
    }

    public interface RepoContentFragmentInteractionListener {
        void onPathChanged(String newPath);
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
}
