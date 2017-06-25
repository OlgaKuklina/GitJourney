package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.parsers.Parser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;
import com.oklab.githubjourney.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olgakuklina on 2017-03-21.
 */

public class UserProfileAsyncTask<T> extends AsyncTask<String, Void, T> {

    private static final String TAG = UserProfileAsyncTask.class.getSimpleName();
    private final Context context;
    private final UserProfileAsyncTask.OnProfilesLoadedListener<T> listener;
    private final Parser<T> parser;
    private UserSessionData currentSessionData;

    public UserProfileAsyncTask(Context context, UserProfileAsyncTask.OnProfilesLoadedListener<T> listener, Parser<T> parser) {
        this.context = context;
        this.listener = listener;
        this.parser = parser;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    @Override
    protected T doInBackground(String... args) {
        String uri;
        if (args.length > 0) {
            String login = args[0];
            uri = context.getString(R.string.url_users, login);
        } else {
            uri = context.getString(R.string.url_user);
        }
        FetchHTTPConnectionService connectionFetcher = new FetchHTTPConnectionService(uri, currentSessionData);
        HTTPConnectionResult result = connectionFetcher.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "response = " + result.getResult());

        try {
            JSONObject jsonObject = new JSONObject(result.getResult());
            return parser.parse(jsonObject);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(T entry) {
        super.onPostExecute(entry);
        listener.OnProfilesLoaded(entry);
    }

    public interface OnProfilesLoadedListener<T> {
        void OnProfilesLoaded(T dataEntry);
    }
}