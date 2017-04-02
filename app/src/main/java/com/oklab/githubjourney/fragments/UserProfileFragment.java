package com.oklab.githubjourney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.adapters.UserProfileDetailAdapter;
import com.oklab.githubjourney.asynctasks.GitHubUserRepositoriesLoader;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by olgakuklina on 2017-03-27.
 */

public class UserProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = StarsListFragment.class.getSimpleName();
    UserProfileDetailAdapter userProfileDetailAdapter;
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private ImageView profilePoster;
    private TextView repositories;
    private TextView stars;
    private TextView following;
    private TextView followers;
    private int currentPage = 1;
    private boolean reposExhausted = false;
    private boolean loading = false;
    private GitHubUserProfileDataEntry entry;
    private GridLayoutManager gridLayoutManager;

    public UserProfileFragment() {
    }

    public static com.oklab.githubjourney.fragments.UserProfileFragment newInstance() {
        com.oklab.githubjourney.fragments.UserProfileFragment fragment = new com.oklab.githubjourney.fragments.UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        entry = getArguments().getParcelable("entry");
        View v = inflater.inflate(R.layout.github_user_profile, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.repo_details);
        recyclerView.setNestedScrollingEnabled(false);
        profilePoster = (ImageView) v.findViewById(R.id.profile_poster);
        repositories = (TextView) v.findViewById(R.id.repositories);
        stars = (TextView) v.findViewById(R.id.stars);
        following = (TextView) v.findViewById(R.id.following);
        followers = (TextView) v.findViewById(R.id.followers);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userProfileDetailAdapter = new UserProfileDetailAdapter(this.getContext());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(userProfileDetailAdapter);
        recyclerView.addOnScrollListener(new UserProfileFragment.UserProfileItemsListOnScrollListener());
        populateUserProfileData();
        Log.v(TAG, "onActivityCreated");
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        bundle.putString("login", entry.getLogin());
        getLoaderManager().initLoader(0, bundle, new UserProfileFragment.ReposLoaderCallbacks());
    }

    @Override
    public void onRefresh() {
        if (loading) {
//            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        userProfileDetailAdapter.resetAllData();
        reposExhausted = false;
        loading = true;
        currentPage = 1;
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        bundle.putString("login", entry.getLogin());
        getLoaderManager().initLoader(0, bundle, new UserProfileFragment.ReposLoaderCallbacks());
    }

    private void populateUserProfileData() {
        repositories.setText(Integer.toString(entry.getPublicRepos()));
        followers.setText(Integer.toString(entry.getFollowers()));
        following.setText(Integer.toString(entry.getFollowing()));

        Log.v(TAG, "entry.getImageUri() = " + entry.getImageUri());
        Picasso pic = Picasso.with(this.getContext());
        if (entry.getImageUri() == null || entry.getImageUri().isEmpty()) {
            pic.load(R.drawable.octocat)
                    .fit().centerCrop()
                    .into(profilePoster);
        } else {
            pic.load(entry.getImageUri())
                    .fit().centerCrop()
                    .error(R.drawable.octocat)
                    .into(profilePoster);
        }
    }

    private class UserProfileItemsListOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastScrollPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemsCount = userProfileDetailAdapter.getItemCount();
            if (lastScrollPosition == itemsCount - 1 && !reposExhausted && !loading) {
                loading = true;
                Bundle bundle = new Bundle();
                bundle.putInt("page", currentPage++);
                bundle.putString("login", entry.getLogin());
                getLoaderManager().initLoader(0, bundle, new UserProfileFragment.ReposLoaderCallbacks());
            }
        }
    }

    private class ReposLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<ReposDataEntry>> {

        @Override
        public Loader<List<ReposDataEntry>> onCreateLoader(int id, Bundle args) {
            Log.v(TAG, "onCreateLoader " + args);
            return new GitHubUserRepositoriesLoader(getContext(), args.getInt("page"), args.getString("login"));
        }

        @Override
        public void onLoadFinished(Loader<List<ReposDataEntry>> loader, List<ReposDataEntry> reposDataEntryList) {
            loading = false;
            if (reposDataEntryList != null && reposDataEntryList.isEmpty()) {
                reposExhausted = true;
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }
            Log.v(TAG, " reposDataEntry = " + reposDataEntryList.get(1).getTitle());
            userProfileDetailAdapter.addAll(reposDataEntryList);
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<ReposDataEntry>> loader) {
            loading = false;
        }
    }
}

