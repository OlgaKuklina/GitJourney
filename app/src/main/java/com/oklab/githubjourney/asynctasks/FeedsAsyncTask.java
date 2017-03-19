package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.R;
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
 * Created by olgakuklina on 2017-01-14.
 */

public class FeedsAsyncTask<T> extends AsyncTask<Integer, Void, List<T>> {

    private static final String TAG = FeedsAsyncTask.class.getSimpleName();
    private final Context context;
    private final OnFeedLoadedListener listener;
    private final AtomParser<T> feedAtomParser;
    private final Object state;
    private UserSessionData currentSessionData;

    public FeedsAsyncTask(Context context, OnFeedLoadedListener listener, AtomParser<T> feedAtomParser, Object state) {
        this.context = context;
        this.listener = listener;
        this.feedAtomParser = feedAtomParser;
        this.state = state;
    }

    public Object getState() {
        return state;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    @Override
    protected List<T> doInBackground(Integer... args) {
        Integer page = args[0];
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

    @Override
    protected void onPostExecute(List<T> entryList) {
        super.onPostExecute(entryList);
        listener.onFeedLoaded(entryList, state);
    }

    public interface OnFeedLoadedListener<T> {
        void onFeedLoaded(List<T> feedDataEntry, Object state);
    }
}
