package com.oklab.githubjourney.asynctasks;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.parsers.FollowersParser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by olgakuklina on 2017-03-29.
 */

public class FollowersLoader extends AsyncTaskLoader<List<GitHubUsersDataEntry>> {
    private static final String TAG = com.oklab.githubjourney.asynctasks.FollowersLoader.class.getSimpleName();
    private final int page;

    public FollowersLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<GitHubUsersDataEntry> loadInBackground() {
        String uri = getContext().getString(R.string.url_followers, page);
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());

        try {
            JSONArray jsonArray = new JSONArray(result.getResult());
            return new FollowersParser().parse(jsonArray);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}

