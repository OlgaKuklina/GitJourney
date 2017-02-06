package com.oklab.githubjourney.fragments;

import android.content.Context;
import android.net.Uri;
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

import com.oklab.githubjourney.adapters.FollowersListAdapter;
import com.oklab.githubjourney.adapters.StarsListAdapter;
import com.oklab.githubjourney.asynctasks.FollowersAsyncTask;
import com.oklab.githubjourney.asynctasks.StarsAsyncTask;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.StarsDataEntry;
import com.oklab.githubjourney.githubjourney.R;

import java.util.List;

/**
 * Created by olgakuklina on 2017-02-06.
 */

public class FollowersListFragment extends Fragment implements FollowersAsyncTask.OnFollowersLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = StarsListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FollowersListAdapter followersListAdapter;
    private FollowersListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean followersExhausted = false;
    private boolean loading = false;

    public FollowersListFragment() {
    }

    public static FollowersListFragment newInstance() {
        FollowersListFragment fragment = new FollowersListFragment();
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
        recyclerView = (RecyclerView) v.findViewById(R.id.items_list_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        followersListAdapter = new FollowersListAdapter(this.getContext());
        recyclerView.setAdapter(followersListAdapter);
        recyclerView.addOnScrollListener(new FollowersListFragment.FollowersItemsListOnScrollListener());
        swipeRefreshLayout.setOnRefreshListener(this);
        loading = true;
        new FollowersAsyncTask(getContext(), this).execute(currentPage++);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FollowersListFragment.OnFragmentInteractionListener) {
            mListener = (FollowersListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        if (loading) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        followersListAdapter.resetAllData();
        followersExhausted = false;
        loading = true;
        currentPage = 1;
        new FollowersAsyncTask(getContext(), this).execute(currentPage++);
    }

    @Override
    public void OnFollowersLoaded(List<GitHubUsersDataEntry> followersDataEntry) {
        loading = false;
        if (followersDataEntry != null && followersDataEntry.isEmpty()) {
            followersExhausted = true;
            return;
        }
        followersListAdapter.add(followersDataEntry);
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class FollowersItemsListOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = followersListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !followersExhausted && !loading) {
                loading = true;
                new FollowersAsyncTask(FollowersListFragment.this.getContext(), FollowersListFragment.this).execute(currentPage++);
            }
        }
    }
}
