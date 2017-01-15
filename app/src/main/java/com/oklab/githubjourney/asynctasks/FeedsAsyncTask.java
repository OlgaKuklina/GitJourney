package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.githubjourney.R;
import com.oklab.githubjourney.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by olgakuklina on 2017-01-14.
 */

public class FeedsAsyncTask extends AsyncTask<Void, Void, Object>{

    private static final String TAG = FeedsAsyncTask.class.getSimpleName();
    private final Context context;
    private UserSessionData currentSessionData;

    public FeedsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    @Override
    protected Object doInBackground(Void... args ) {
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_feeds)).openConnection();
            connect.setRequestMethod("GET");

            String authentication  = "basic " + currentSessionData.getCredentials();
            connect.setRequestProperty("Authorization", authentication);

            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            if(responseCode!= HttpStatus.SC_OK) {
                return null;
            }
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            JSONObject jObj = new  JSONObject(response);

            String currentUserURL = jObj.getString("CurrentUserURL");
            Log.v(TAG, "currentUserURL = " + currentUserURL);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Get user feeds failed", e);
            return null;
        }
    }
}
