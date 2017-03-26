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

import com.oklab.githubjourney.adapters.FollowingListAdapter;
import com.oklab.githubjourney.asynctasks.FollowingAsyncTask;
import com.oklab.githubjourney.asynctasks.UserProfileAsyncTask;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.R;
import com.oklab.githubjourney.parsers.GitHubUserProfileDataParser;
import com.oklab.githubjourney.parsers.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-06.
 */

public class FollowingListFragment extends Fragment implements FollowingAsyncTask.OnFollowingLoadedListener,UserProfileAsyncTask.OnProfilesLoadedListener<GitHubUserProfileDataEntry>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = FollowingListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FollowingListAdapter followingListAdapter;
    private FollowingListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<GitHubUserProfileDataEntry> profileDataEntryList;
    private int count = 0;
    private int currentPage = 1;
    private boolean followingExhausted = false;
    private boolean loading = false;

    public FollowingListFragment() {
    }

    public static FollowingListFragment newInstance() {
        FollowingListFragment fragment = new FollowingListFragment();
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
        followingListAdapter = new FollowingListAdapter(this.getContext());
        recyclerView.setAdapter(followingListAdapter);
        recyclerView.addOnScrollListener(new FollowingListFragment.FollowingItemsListOnScrollListener());
        swipeRefreshLayout.setOnRefreshListener(this);
        loading = true;
        new FollowingAsyncTask(getContext(), this).execute(currentPage++);
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
        if (context instanceof FollowingListFragment.OnFragmentInteractionListener) {
            mListener = (FollowingListFragment.OnFragmentInteractionListener) context;
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
        followingListAdapter.resetAllData();
        followingExhausted = false;
        loading = true;
        currentPage = 1;
        new FollowingAsyncTask(getContext(), this).execute(currentPage++);
    }

    @Override
    public void onFollowingLoaded(List<GitHubUsersDataEntry> followingDataEntryList) {
        loading = false;
        if (followingDataEntryList != null && followingDataEntryList.isEmpty()) {
            followingExhausted = true;
            return;
        }
        Parser<GitHubUserProfileDataEntry> parser = new GitHubUserProfileDataParser();
        count = followingDataEntryList.size();
        profileDataEntryList = new ArrayList<>(count);
        for(GitHubUsersDataEntry entry: followingDataEntryList) {
            new UserProfileAsyncTask<GitHubUserProfileDataEntry>(getContext(), this, parser).execute(entry.getLogin());
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void OnProfilesLoaded(GitHubUserProfileDataEntry profileDataEntry) {
        Log.v(TAG, "OnProfilesLoaded " + count + " , " +  profileDataEntry);
        count--;
        if (profileDataEntry != null && profileDataEntry.getLocation()!=null && !profileDataEntry.getLocation().isEmpty()) {
            profileDataEntryList.add(profileDataEntry);
        }
        if(count == 0) {
            followingListAdapter.add(profileDataEntryList);
        }
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

    private class FollowingItemsListOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = followingListAdapter.getItemCount();
            Log.v(TAG, "onScrolled - imetsCount = " + itemsCount);
            Log.v(TAG, "onScrolled - lastScrollPosition = " + lastScrollPosition);
            if (lastScrollPosition == itemsCount - 1 && !followingExhausted && !loading) {
                loading = true;
                new FollowingAsyncTask(FollowingListFragment.this.getContext(), FollowingListFragment.this).execute(currentPage++);
            }
        }
    }
}