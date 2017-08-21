package com.oklab.gitjourney.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.asynctasks.RepoReadmeDownloadAsyncTask;
import com.oklab.gitjourney.data.ReposDataEntry;
import com.oklab.gitjourney.data.UserSessionData;
import com.oklab.gitjourney.fragments.CommitsFragment;
import com.oklab.gitjourney.fragments.ContributorsFragment;
import com.oklab.gitjourney.fragments.EventsFragment;
import com.oklab.gitjourney.fragments.IssuesFragment;
import com.oklab.gitjourney.fragments.ReadmeFragment;
import com.oklab.gitjourney.fragments.RepositoryContentListFragment;
import com.oklab.gitjourney.services.TakeScreenshotService;
import com.oklab.gitjourney.utils.GithubLanguageColorsMatcher;
import com.oklab.gitjourney.utils.Utils;

import org.markdownj.MarkdownProcessor;

public class RepositoryActivity extends AppCompatActivity implements RepoReadmeDownloadAsyncTask.OnRepoReadmeContentLoadedListener
        , RepositoryContentListFragment.RepoContentFragmentInteractionListener
        , CommitsFragment.OnFragmentInteractionListener
        , ContributorsFragment.OnListFragmentInteractionListener {
    private static final String TAG = RepositoryActivity.class.getSimpleName();
    private WebView mv;
    private String owner = "";
    private String title = "";
    private UserSessionData currentSessionData;
    private TakeScreenshotService takeScreenshotService;
    private RepositoryContentListFragment repoContentListFragment;
    private ViewPager mViewPager;
    private RepositoryActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ReposDataEntry entry = getIntent().getParcelableExtra("repo");
        title = entry.getTitle();
        toolbar.setTitleMarginBottom(3);
        toolbar.setTitle(title);

        mSectionsPagerAdapter = new RepositoryActivity.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.repo_view_pager);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(4);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.repo_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (entry.getLanguage() != null && !entry.getLanguage().isEmpty() && !entry.getLanguage().equals("null")) {
            Log.v(TAG, " data.getLanguage() = " + entry.getLanguage());
            int colorId = GithubLanguageColorsMatcher.findMatchedColor(this, entry.getLanguage());
            Log.v(TAG, " colorId = " + colorId);
            if (colorId != 0) {
                toolbar.setBackgroundColor(this.getResources().getColor(colorId));
            }
        }
        setSupportActionBar(toolbar);
        if (entry.getOwner() == null || entry.getOwner().isEmpty()) {
            SharedPreferences prefs = this.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
            String sessionDataStr = prefs.getString("userSessionData", null);
            currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
            owner = currentSessionData.getLogin();
        } else {
            owner = entry.getOwner();
        }
        repoContentListFragment = RepositoryContentListFragment.newInstance(this, entry.getTitle(), owner);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, repoContentListFragment).commit();
        mv = (WebView) findViewById(R.id.web_view);
        mv.setWebViewClient(new WebViewClient());
        takeScreenshotService = new TakeScreenshotService(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshotService.takeScreenShot();
            }
        });
        new RepoReadmeDownloadAsyncTask(this, this).execute(entry.getTitle(), owner);
    }

    @Override
    public void onRepoContentLoaded(String content) {
        Log.v(TAG, "onRepoContentLoaded");
        Log.v(TAG, "content " + content);
        if (content != null) {
            Log.v(TAG, "if content " + content);
            MarkdownProcessor processor = new MarkdownProcessor();
            String html = processor.markdown(content);
            mv.setVisibility(View.VISIBLE);
            mv.loadData(html, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    public void onPathChanged(String newPath) {
        Log.v(TAG, "newPath onPathChanged = " + newPath);
        if (newPath != null && !newPath.isEmpty()) {
            mv.setVisibility(View.GONE);
        }
        if (newPath.isEmpty()) {
            mv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (repoContentListFragment.isVisible()) {
            if (!repoContentListFragment.repoContentOnBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(String item) {

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
        public static GeneralActivity.PlaceholderFragment newInstance(int sectionNumber) {
            Log.v(TAG, "newInstance ");
            GeneralActivity.PlaceholderFragment fragment = new GeneralActivity.PlaceholderFragment();
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
                    return ReadmeFragment.newInstance("d", "d");
                case 1:
                    return RepositoryContentListFragment.newInstance(RepositoryActivity.this, title, owner);
                case 2:
                    return CommitsFragment.newInstance("d", "d");
                case 3:
                    return EventsFragment.newInstance("d", "d");
                case 4:
                    return IssuesFragment.newInstance("d", "d");
                case 5:
                    return ContributorsFragment.newInstance(1);
            }
            return RepositoryActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.v(TAG, "getPageTitle ");
            switch (position) {
                case 0:
                    return getApplicationContext().getString(R.string.readme);
                case 1:
                    return getApplicationContext().getString(R.string.code);
                case 2:
                    return getApplicationContext().getString(R.string.commits);
                case 3:
                    return getApplicationContext().getString(R.string.events);
                case 4:
                    return getApplicationContext().getString(R.string.issues);
                case 5:
                    return getApplicationContext().getString(R.string.contributors);
            }
            return null;
        }
    }
}
