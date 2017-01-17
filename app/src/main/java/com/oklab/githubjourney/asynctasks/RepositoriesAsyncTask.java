package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.data.FeedDataEntry;
import com.oklab.githubjourney.data.RepositoriesDataEntry;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.githubjourney.R;
import com.oklab.githubjourney.services.AtomParser;
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
 * Created by olgakuklina on 2017-01-16.
 */

public class RepositoriesAsyncTask extends AsyncTask<Integer, Void, List<RepositoriesDataEntry>> {
    private static final String TAG = RepositoriesAsyncTask.class.getSimpleName();
    private final Context context;
    private final OnRepositoriesLoadedListener listener;
    private UserSessionData currentSessionData;

    public RepositoriesAsyncTask(Context context, OnRepositoriesLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<RepositoriesDataEntry> doInBackground(Integer... args) {
        int page = args[0];
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_repos)).openConnection();
            connect.setRequestMethod("GET");

            String authentication = "token " + currentSessionData.getToken();
            connect.setRequestProperty("Authorization", authentication);

            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);

            return null;

        } catch (Exception e) {
            Log.e(TAG, "Get user feeds failed", e);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }
    public interface OnRepositoriesLoadedListener {
        void onRepositoriesLoaded(List<FeedDataEntry> feedDataEntry);
    }

}
