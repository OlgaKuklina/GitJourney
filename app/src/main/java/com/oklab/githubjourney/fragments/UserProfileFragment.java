package com.oklab.githubjourney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.asynctasks.UserProfileAsyncTask;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.oklab.githubjourney.parsers.GitHubUserProfileDataParser;
import com.oklab.githubjourney.parsers.Parser;
import com.squareup.picasso.Picasso;

/**
 * Created by olgakuklina on 2017-03-27.
 */

public class UserProfileFragment extends Fragment implements UserProfileAsyncTask.OnProfilesLoadedListener<GitHubUserProfileDataEntry>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = StarsListFragment.class.getSimpleName();
    private ScrollView scrollView;
    private ImageView profilePoster;
    private TextView repositories;
    private TextView stars;
    private TextView following;
    private TextView followers;
    private LinearLayoutManager linearLayoutManager;
    private int count = 6;

    public UserProfileFragment() {
    }

    public static com.oklab.githubjourney.fragments.UserProfileFragment newInstance() {
        com.oklab.githubjourney.fragments.UserProfileFragment fragment = new com.oklab.githubjourney.fragments.UserProfileFragment();
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
        View v = inflater.inflate(R.layout.github_user_profile, container, false);
        scrollView = (ScrollView) v.findViewById(R.id.scroll_view);
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
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        Parser<GitHubUserProfileDataEntry> parser = new GitHubUserProfileDataParser();
        new UserProfileAsyncTask<GitHubUserProfileDataEntry>(getContext(), com.oklab.githubjourney.fragments.UserProfileFragment.this, parser).execute(getArguments().getString("login_id"));
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void OnProfilesLoaded(GitHubUserProfileDataEntry profileDataEntry) {

        Log.v(TAG, "OnProfilesLoaded " + count + " , " + profileDataEntry);
        if (profileDataEntry != null && profileDataEntry.getLocation() != null && !profileDataEntry.getLocation().isEmpty()) {
            populateUserProfileData(profileDataEntry);
        }
    }

    private void populateUserProfileData(GitHubUserProfileDataEntry profileDataEntry) {
        repositories.setText(Integer.toString(profileDataEntry.getPublicRepos()));
        followers.setText(Integer.toString(profileDataEntry.getFollowers()));
        following.setText(Integer.toString(profileDataEntry.getFollowing()));
        Picasso pic = Picasso.with(this.getContext());
        if (profileDataEntry.getImageUri() == null || profileDataEntry.getImageUri().isEmpty()) {
            pic.load(R.drawable.octocat)
                    .fit().centerCrop()
                    .into(profilePoster);
        } else {
            pic.load(profileDataEntry.getImageUri())
                    .fit().centerCrop()
                    .error(R.drawable.octocat)
                    .into(profilePoster);
        }

    }
}

