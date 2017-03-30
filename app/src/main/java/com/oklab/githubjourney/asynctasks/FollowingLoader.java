package com.oklab.githubjourney.asynctasks;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.parsers.FollowersParser;
import com.oklab.githubjourney.parsers.Parser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-29.
 */

public class FollowingLoader extends AsyncTaskLoader<List<GitHubUsersDataEntry>> {
    private static final String TAG = FollowingLoader.class.getSimpleName();
    private final int page;

    public FollowingLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    public List<GitHubUsersDataEntry> loadInBackground() {
        String uri = getContext().getString(R.string.url_following, page);
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
