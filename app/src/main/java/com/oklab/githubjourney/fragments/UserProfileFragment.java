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
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by olgakuklina on 2017-03-27.
 */

public class UserProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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
        populateUserProfileData();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onRefresh() {
    }

    private void populateUserProfileData() {
        GitHubUserProfileDataEntry entry = getArguments().getParcelable("entry");
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
}

