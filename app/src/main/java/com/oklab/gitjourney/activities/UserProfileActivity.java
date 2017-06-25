package com.oklab.gitjourney.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.data.GitHubUserProfileDataEntry;
import com.oklab.gitjourney.fragments.UserProfileFragment;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GitHubUserProfileDataEntry entry = getIntent().getParcelableExtra("profile");
        Log.v(TAG, "entry " + entry.getImageUri());
        getSupportActionBar().setTitle(entry.getName());
        getSupportActionBar().setSubtitle(entry.getLocation());
        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("entry", entry);
        userProfileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_profile_fragment, userProfileFragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
