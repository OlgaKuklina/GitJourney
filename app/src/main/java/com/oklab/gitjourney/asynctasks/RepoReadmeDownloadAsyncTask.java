package com.oklab.gitjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.data.HTTPConnectionResult;
import com.oklab.gitjourney.data.UserSessionData;
import com.oklab.gitjourney.services.FetchHTTPConnectionService;
import com.oklab.gitjourney.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by olgakuklina on 2017-04-25.
 */

public class RepoReadmeDownloadAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = RepoReadmeDownloadAsyncTask.class.getSimpleName();
    private final Context context;
    private final OnRepoReadmeContentLoadedListener listener;
    private UserSessionData currentSessionData;

    public RepoReadmeDownloadAsyncTask(Context context, OnRepoReadmeContentLoadedListener listener) {
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
    protected String doInBackground(String... args) {
        String repo = args[0];
        String login = args[1];
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_repo_readme, login, repo)).openConnection();
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
            JSONObject jsonObj = new JSONObject(response);
            String downloadUrl = jsonObj.getString("download_url");
            FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(downloadUrl, context);
            HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
            Log.v(TAG, "responseCode = " + result.getResponceCode());
            Log.v(TAG, "result = " + result.getResult());
            return result.getResult();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String content) {
        super.onPostExecute(content);
        listener.onRepoContentLoaded(content);
    }

    public interface OnRepoReadmeContentLoadedListener {
        void onRepoContentLoaded(String content);
    }
}

