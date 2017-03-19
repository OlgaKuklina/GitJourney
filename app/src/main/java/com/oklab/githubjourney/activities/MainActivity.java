package com.oklab.githubjourney.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.oklab.githubjourney.data.UpdaterService;
import com.oklab.githubjourney.fragments.MainViewFragment;
import com.oklab.githubjourney.R;
import com.oklab.githubjourney.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences prefs;
    private ViewPager calendarYearviewPager;
    private CalendarYearPagerAdapter calendarYearPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendarYearPagerAdapter = new CalendarYearPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        calendarYearviewPager = (ViewPager) findViewById(R.id.pager);
        calendarYearviewPager.setAdapter(calendarYearPagerAdapter);

        prefs = this.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String currentSessionData = prefs.getString("userSessionData", null);
        if (currentSessionData == null) {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            return;
        }

        this.startService(new Intent(this, UpdaterService.class));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            String currentSessionData = prefs.getString("userSessionData", null);
            if (currentSessionData == null) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.github_events) {

        } else if (id == R.id.profile) {
            String currentSessionData = prefs.getString("userSessionData", null);
            if (currentSessionData != null) {
                Intent intent = new Intent(this, GeneralActivity.class);
                startActivity(intent);
                return true;
            }
        } else if (id == R.id.settings) {

        } else if (id == R.id.sing_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class CalendarYearPagerAdapter extends FragmentPagerAdapter {

        public CalendarYearPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Log.v(TAG, "position - " + position);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MainViewFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 12;
        }
    }
}
