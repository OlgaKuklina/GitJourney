package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;
import com.oklab.githubjourney.parsers.RepoContentParser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by olgakuklina on 2017-04-17.
 */

public class RepoContentLoader extends AsyncTaskLoader<List<RepositoryContentDataEntry>> {
    private static final String TAG = RepoContentLoader.class.getSimpleName();
    private final String path;
    private final String repoName;
    private final String userName;

    public RepoContentLoader(Context context, String path, String repoName, String userName) {
        super(context);
        this.path = path;
        this.repoName = repoName;
        this.userName = userName;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<RepositoryContentDataEntry> loadInBackground() {
        String uri = getContext().getString(R.string.url_repo_content, userName, repoName, path);
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());

        try {
            JSONArray jsonArray = new JSONArray(result.getResult());
            return new RepoContentParser().parse(jsonArray);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}