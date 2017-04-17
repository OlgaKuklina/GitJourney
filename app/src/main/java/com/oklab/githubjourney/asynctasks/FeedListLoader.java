package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.parsers.AtomParser;
import com.oklab.githubjourney.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-30.
 */

public class FeedListLoader<T> extends AsyncTaskLoader<List<T>> {
    private static final String TAG = com.oklab.githubjourney.asynctasks.FollowersLoader.class.getSimpleName();
    private final int page;
    private final AtomParser<T> feedAtomParser;
    private UserSessionData currentSessionData;
    private Context context;

    public FeedListLoader(Context context, int page, AtomParser<T> feedAtomParser) {
        super(context);
        this.context = context;
        this.page = page;
        this.feedAtomParser = feedAtomParser;
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<T> loadInBackground() {
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_feeds, page)).openConnection();
            connect.setRequestMethod("GET");

            String authentication = "basic " + currentSessionData.getCredentials();
            connect.setRequestProperty("Authorization", authentication);

            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            if (responseCode != HttpStatus.SC_OK) {
                return null;
            }
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            JSONObject jObj = new JSONObject(response);

            String currentUserURL = jObj.getString("current_user_url") + "&page=" + page;
            Log.v(TAG, "currentUserURL = " + currentUserURL);
            return feedAtomParser.parse(currentUserURL);

        } catch (Exception e) {
            Log.e(TAG, "Get user feeds failed", e);
            return null;
        }
    }
}