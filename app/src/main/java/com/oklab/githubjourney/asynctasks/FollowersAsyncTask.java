package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.data.GitHubUsersDataEntry;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.R;
import com.oklab.githubjourney.services.FollowersParser;
import com.oklab.githubjourney.utils.Utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by olgakuklina on 2017-01-20.
 */

public class FollowersAsyncTask extends AsyncTask<Integer, Void, List<GitHubUsersDataEntry>> {

    private static final String TAG = FollowersAsyncTask.class.getSimpleName();
    private final Context context;
    private UserSessionData currentSessionData;
    private FollowersAsyncTask.OnFollowersLoadedListener listener;

    public FollowersAsyncTask(Context context, FollowersAsyncTask.OnFollowersLoadedListener listener) {
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
    protected List<GitHubUsersDataEntry> doInBackground(Integer... args) {
        Integer page = args[0];
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_followers, page)).openConnection();
            connect.setRequestMethod("GET");

            String authentication = "token " + currentSessionData.getToken();
            connect.setRequestProperty("Authorization", authentication);

            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            JSONArray jsonArray = new JSONArray(response);

            return new FollowersParser().parse(jsonArray);

        } catch (Exception e) {
            Log.e(TAG, "Get user feeds failed", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<GitHubUsersDataEntry> entryList) {
        super.onPostExecute(entryList);
        listener.OnFollowersLoaded(entryList);
    }

    public interface OnFollowersLoadedListener {
        void OnFollowersLoaded(List<GitHubUsersDataEntry> followersDataEntry);
    }
}
