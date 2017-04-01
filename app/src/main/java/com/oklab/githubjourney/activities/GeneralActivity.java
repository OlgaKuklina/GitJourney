package com.oklab.githubjourney.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.oklab.githubjourney.R;
import com.oklab.githubjourney.adapters.FirebaseAnalyticsWrapper;
import com.oklab.githubjourney.fragments.FeedListFragment;
import com.oklab.githubjourney.fragments.FollowersListFragment;
import com.oklab.githubjourney.fragments.FollowingListFragment;
import com.oklab.githubjourney.fragments.LocationsReadyCallback;
import com.oklab.githubjourney.fragments.RepositoriesListFragment;
import com.oklab.githubjourney.fragments.StarsListFragment;
import com.oklab.githubjourney.services.TakeScreenshotService;

import java.util.TimeZone;

public class GeneralActivity extends AppCompatActivity implements FeedListFragment.OnFragmentInteractionListener, RepositoriesListFragment.OnFragmentInteractionListener, StarsListFragment.OnFragmentInteractionListener, FollowersListFragment.OnFragmentInteractionListener, FollowingListFragment.OnFragmentInteractionListener {

    private static final String TAG = GeneralActivity.class.getSimpleName();
    private TakeScreenshotService takeScreenshotService;
    private FirebaseAnalyticsWrapper firebaseAnalytics;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_general);
        firebaseAnalytics = new FirebaseAnalyticsWrapper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        String timezone = TimeZone.getDefault().toString();
        Log.v(TAG, "onCreate timezone = " + timezone);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v(TAG, "onPageSelected, position = " + position);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(position));
                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "GeneralActivityTabChange");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(5);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        takeScreenshotService = new TakeScreenshotService(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "takeScreenShot");
                takeScreenshotService.takeScreenShot();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAnalytics.setCurrentScreen(this, "GeneralActivityFirebaseAnalytics");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.v(TAG, "onFragmentInteraction ");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            Log.v(TAG, "newInstance ");
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "onCreateView ");
            View rootView = inflater.inflate(R.layout.fragment_general_list, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Log.v(TAG, "getItem, position = " + position);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return FeedListFragment.newInstance();
                case 1:
                    return RepositoriesListFragment.newInstance();
                case 2:
                    return FollowingListFragment.newInstance();
                case 3:
                    return FollowersListFragment.newInstance();
                case 4:
                    return StarsListFragment.newInstance();
                case 5:
                    LocationsReadyCallback callback = new LocationsReadyCallback(GeneralActivity.this);
                    SupportMapFragment fragment = SupportMapFragment.newInstance();
                    fragment.getMapAsync(callback);
                    return fragment;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(GeneralActivity.this);
            boolean map = sharedPref.getBoolean("map_switch", true);
            Log.v(TAG, "map value = " + map);
            if (map) {
                return 6;
            } else {
                return 5;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.v(TAG, "getPageTitle ");
            switch (position) {
                case 0:
                    return getApplicationContext().getString(R.string.feed);
                case 1:
                    return getApplicationContext().getString(R.string.repositories);
                case 2:
                    return getApplicationContext().getString(R.string.following);
                case 3:
                    return getApplicationContext().getString(R.string.followers);
                case 4:
                    return getApplicationContext().getString(R.string.stars);
                case 5:
                    return getApplicationContext().getString(R.string.map);
            }
            return null;
        }
    }
}
