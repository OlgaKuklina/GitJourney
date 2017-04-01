package com.oklab.githubjourney.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.fragments.UserProfileFragment;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = getIntent().getStringExtra("name");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("login_id", getIntent().getStringExtra("login_id"));
        userProfileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_profile_fragment, userProfileFragment).commit();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
