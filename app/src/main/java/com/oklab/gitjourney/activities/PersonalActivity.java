package com.oklab.gitjourney.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.adapters.UserProfileDetailAdapter;
import com.oklab.gitjourney.asynctasks.GitHubUserRepositoriesLoader;
import com.oklab.gitjourney.asynctasks.RepositoriesLoader;
import com.oklab.gitjourney.asynctasks.UserProfileAsyncTask;
import com.oklab.gitjourney.data.GitHubUserProfileDataEntry;
import com.oklab.gitjourney.data.ReposDataEntry;
import com.oklab.gitjourney.parsers.GitHubUserProfileDataParser;
import com.oklab.gitjourney.parsers.Parser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class PersonalActivity extends AppCompatActivity implements UserProfileAsyncTask.OnProfilesLoadedListener<GitHubUserProfileDataEntry> {
    private static final String TAG = PersonalActivity.class.getSimpleName();
    UserProfileDetailAdapter userProfileDetailAdapter;
    private RecyclerView recyclerView;
    private ImageView profilePoster;
    private TextView repositories;
    private TextView following;
    private TextView followers;
    private GitHubUserProfileDataEntry entry;
    private int currentPage = 1;
    private boolean reposExhausted = false;
    private boolean loading = false;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userLogin = getIntent().getStringExtra("userSessionData");

        setContentView(R.layout.activity_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(userLogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.repo_details);
        recyclerView.setNestedScrollingEnabled(false);
        profilePoster = (ImageView) findViewById(R.id.profile_poster);
        repositories = (TextView) findViewById(R.id.repositories);
        following = (TextView) findViewById(R.id.following);
        followers = (TextView) findViewById(R.id.followers);

        Parser<GitHubUserProfileDataEntry> parser = new GitHubUserProfileDataParser();
        new UserProfileAsyncTask<GitHubUserProfileDataEntry>(this, this, parser).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE); //TODO: delete this line when edit functionality will be done
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: implement an edit activity
            }
        });
    }

    @Override
    public void OnProfilesLoaded(GitHubUserProfileDataEntry dataEntry) {
        entry = dataEntry;
        userProfileDetailAdapter = new UserProfileDetailAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(userProfileDetailAdapter);
        recyclerView.addOnScrollListener(new UserProfileItemsListOnScrollListener());
        populateUserProfileData();
        Log.v(TAG, "onActivityCreated");
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage++);
        bundle.putString("login", dataEntry.getLogin());
        getSupportLoaderManager().initLoader(0, bundle, new ReposLoaderCallbacks());
    }

    private void populateUserProfileData() {
        repositories.setText(String.format(Locale.getDefault(), "%d", entry.getPublicRepos()));
        followers.setText(String.format(Locale.getDefault(), "%d", entry.getFollowers()));
        following.setText(String.format(Locale.getDefault(), "%d", entry.getFollowing()));

        Picasso pic = Picasso.with(this);
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
                getSupportLoaderManager().initLoader(0, bundle, new ReposLoaderCallbacks());
            }
        }
    }

    private class ReposLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<ReposDataEntry>> {

        @Override
        public Loader<List<ReposDataEntry>> onCreateLoader(int id, Bundle args) {
            return new RepositoriesLoader(PersonalActivity.this, args.getInt("page"));
        }

        @Override
        public void onLoadFinished(Loader<List<ReposDataEntry>> loader, List<ReposDataEntry> reposDataEntryList) {
            loading = false;
            if (reposDataEntryList != null && reposDataEntryList.isEmpty()) {
                reposExhausted = true;
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }
            userProfileDetailAdapter.addAll(reposDataEntryList);
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<List<ReposDataEntry>> loader) {
            loading = false;
        }
    }
}

