package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.GitHubUserLocationDataEntry;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;
import com.oklab.githubjourney.parsers.LocationDataParser;
import com.oklab.githubjourney.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-21.
 */

public class UserProfileAsyncTask extends AsyncTask<String, Void, GitHubUserLocationDataEntry> {

    private static final String TAG = UserProfileAsyncTask.class.getSimpleName();
    private final Context context;
    private UserSessionData currentSessionData;
    private UserProfileAsyncTask.OnProfilesLoadedListener listener;

    public UserProfileAsyncTask(Context context, UserProfileAsyncTask.OnProfilesLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    @Override
    protected GitHubUserLocationDataEntry doInBackground(String... args) {
        String login  = args[0];
        String Uri = context.getString(R.string.url_users, login);
        FetchHTTPConnectionService connectionFetcher = new FetchHTTPConnectionService(Uri, currentSessionData);
        HTTPConnectionResult result = connectionFetcher.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "response = " + result.getResult());

        try {
            JSONObject jsonObject = new JSONObject(result.getResult());
            return new LocationDataParser().parse(jsonObject);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }


    @Override
    protected void onPostExecute(GitHubUserLocationDataEntry entry) {
        super.onPostExecute(entry);
        listener.OnProfilesLoaded(entry);
    }

    public interface OnProfilesLoadedListener {
        void OnProfilesLoaded(GitHubUserLocationDataEntry followersDataEntry);
    }
}