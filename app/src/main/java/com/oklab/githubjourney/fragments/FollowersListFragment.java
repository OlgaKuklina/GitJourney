package com.oklab.githubjourney.fragments;

import android.content.Context;
import android.net.Uri;
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
import com.oklab.githubjourney.adapters.FollowersListAdapter;
import com.oklab.githubjourney.asynctasks.FollowersLoader;
import com.oklab.githubjourney.asynctasks.UserProfileAsyncTask;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.parsers.GitHubUserProfileDataParser;
import com.oklab.githubjourney.parsers.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-06.
 */

public class FollowersListFragment extends Fragment implements UserProfileAsyncTask.OnProfilesLoadedListener<GitHubUserProfileDataEntry>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = StarsListFragment.class.getSimpleName();
    ArrayList<GitHubUserProfileDataEntry> profileDataEntryList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FollowersListAdapter followersListAdapter;
    private FollowersListFragment.OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private boolean followersExhausted = false;
    private boolean loading = false;
    private int count = 0;

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
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        getLoaderManager().initLoader(0, bundle, new FollowersListFragment.FollowersLoaderCallbacks());
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
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        getLoaderManager().initLoader(0, bundle, new FollowersListFragment.FollowersLoaderCallbacks());
    }

    @Override
    public void OnProfilesLoaded(GitHubUserProfileDataEntry profileDataEntry) {

        Log.v(TAG, "OnProfilesLoaded " + count + " , " + profileDataEntry);
        count--;
        if (profileDataEntry != null && profileDataEntry.getLocation() != null && !profileDataEntry.getLocation().isEmpty()) {
            profileDataEntryList.add(profileDataEntry);
        }
        if (count == 0) {
            followersListAdapter.add(profileDataEntryList);
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
                Bundle bundle = new Bundle();
                bundle.putInt("page", currentPage++);
                getLoaderManager().initLoader(0, bundle, new FollowersListFragment.FollowersLoaderCallbacks());
            }
        }
    }

    private class FollowersLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<GitHubUsersDataEntry>> {

        @Override
        public Loader<List<GitHubUsersDataEntry>> onCreateLoader(int id, Bundle args) {
            Log.v(TAG, "onCreateLoader " + args);
            return new FollowersLoader(getContext(), args.getInt("page"));
        }

        @Override
        public void onLoadFinished(Loader<List<GitHubUsersDataEntry>> loader, List<GitHubUsersDataEntry> followersDataEntryList) {
            loading = false;
            if (followersDataEntryList != null && followersDataEntryList.isEmpty()) {
                followersExhausted = true;
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }
            Parser<GitHubUserProfileDataEntry> parser = new GitHubUserProfileDataParser();
            count = followersDataEntryList.size();
            profileDataEntryList = new ArrayList<>(count);
            for (GitHubUsersDataEntry entry : followersDataEntryList) {
                new UserProfileAsyncTask<GitHubUserProfileDataEntry>(getContext(), FollowersListFragment.this, parser).execute(entry.getLogin());
            }
            swipeRefreshLayout.setRefreshing(false);
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<GitHubUsersDataEntry>> loader) {
            Log.v(TAG, "onLoaderReset");
            loading = false;
        }
    }
}
