package com.oklab.githubjourney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean reposContentExhausted = false;
    private boolean loading = false;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        repoContentListAdapter = new RepoContentListAdapter(this.getContext());
        recyclerView.setAdapter(repoContentListAdapter);
        recyclerView.addOnScrollListener(new RepositoryContentListFragment.RepoContentItemsListOnScrollListner());
        swipeRefreshLayout.setOnRefreshListener(this);
        loading = true;
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
    }

    @Override
    public void onRefresh() {

    }

    private class RepoContentItemsListOnScrollListner extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = repoContentListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !reposContentExhausted && !loading) {
                loading = true;
                Bundle bundle = new Bundle();
                bundle.putInt("page", currentPage++);
            }
        }
    }
}
