package com.oklab.githubjourney.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.asynctasks.RepoReadmeDownloadAsyncTask;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.fragments.RepositoryContentListFragment;
import com.oklab.githubjourney.services.TakeScreenshotService;
import com.oklab.githubjourney.utils.GithubLanguageColorsMatcher;
import com.oklab.githubjourney.utils.Utils;

import org.markdownj.MarkdownProcessor;

public class RepositoryActivity extends AppCompatActivity implements RepoReadmeDownloadAsyncTask.OnRepoReadmeContentLoadedListener, RepositoryContentListFragment.RepoContentFragmentInteractionListener {
    private static final String TAG = RepositoryActivity.class.getSimpleName();
    private WebView mv;
    private String owner = "";
    private String title = "";
    private UserSessionData currentSessionData;
    private TakeScreenshotService takeScreenshotService;
    private RepositoryContentListFragment repoContentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ReposDataEntry entry = getIntent().getParcelableExtra("repo");
        title = entry.getTitle();
        toolbar.setTitle(title);
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
        new RepoReadmeDownloadAsyncTask(this, this).execute(entry.getTitle());
    }

    @Override
    public void onRepoContentLoaded(String content) {
        Log.v(TAG, "onRepoContentLoaded");
        Log.v(TAG, "content" + content);
        if (content != null) {
            Log.v(TAG, "if content " + content);
            MarkdownProcessor processor = new MarkdownProcessor();
            String html = processor.markdown(content);
            mv.setVisibility(View.VISIBLE);
            mv.loadData(html, "text/html; charset=UTF-8", null);
        } else {
            Log.v(TAG, "else content " + content);
            mv.loadData("no README.md file", "text/html; charset=UTF-8", null);
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
}
