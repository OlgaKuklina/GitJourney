package com.oklab.githubjourney.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.asynctasks.RepoReadmeAsyncTask;
import com.oklab.githubjourney.asynctasks.RepoReadmeDownloadAsyncTask;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;
import com.oklab.githubjourney.data.ReposDataEntry;

import org.markdownj.MarkdownProcessor;

import java.util.List;

public class RepositoryActivity extends AppCompatActivity implements RepoReadmeDownloadAsyncTask.OnRepoReadmeContentLoadedListener{
    private static final String TAG = RepositoryActivity.class.getSimpleName();
    private WebView mv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ReposDataEntry entry = getIntent().getParcelableExtra("repo");
        toolbar.setTitle(entry.getTitle());
        setSupportActionBar(toolbar);
        mv = (WebView) findViewById(R.id.webView);
        mv.setWebViewClient(new WebViewClient());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new RepoReadmeDownloadAsyncTask(this, this).execute(entry.getTitle());
    }
    @Override
    public void onRepoContentLoaded(String content) {
        Log.v(TAG, "onRepoContentLoaded");
        Log.v(TAG, "content" + content);
        if(content !=null) {
            MarkdownProcessor processor = new MarkdownProcessor();
            String html = processor.markdown(content);
            mv.loadData(html, "text/html; charset=UTF-8", null);
        }
        else {
            mv.loadData("<h1>no README.md file</h1>", "text/html; charset=UTF-8", null);
        }

    }
}
