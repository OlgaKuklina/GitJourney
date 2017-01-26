package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.data.FeedDataEntry;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.githubjourney.R;
import com.oklab.githubjourney.services.ReposParser;
import com.oklab.githubjourney.utils.Utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class RepositoriesAsyncTask extends AsyncTask<Integer, Void, List<ReposDataEntry>> {
    private static final String TAG = RepositoriesAsyncTask.class.getSimpleName();
    private final Context context;
    private final OnReposLoadedListener listener;
    private UserSessionData currentSessionData;

    public RepositoriesAsyncTask(Context context, OnReposLoadedListener listener) {
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
    protected List<ReposDataEntry> doInBackground(Integer... args) {
        int page = args[0];
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_repos)).openConnection();
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

            return new ReposParser().parse(jsonArray);

        } catch (Exception e) {
            Log.e(TAG, "Get user feeds failed", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ReposDataEntry> entryList) {
        super.onPostExecute(entryList);
        listener.onReposLoaded(entryList);
    }
    public interface OnReposLoadedListener {
        void onReposLoaded(List<ReposDataEntry> reposDataEntry);
    }

}
