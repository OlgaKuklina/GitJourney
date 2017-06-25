package com.oklab.gitjourney.asynctasks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.activities.AuthenticationActivity;
import com.oklab.gitjourney.data.UserSessionData;
import com.oklab.gitjourney.utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olgakuklina on 2017-03-26.
 */

public class DeleteUserAuthorizationAsyncTask extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = DeleteUserAuthorizationAsyncTask.class.getSimpleName();
    private final Activity activity;
    private UserSessionData currentSessionData;

    public DeleteUserAuthorizationAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... args) {
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(activity.getString(R.string.url_disconnect, currentSessionData.getId())).openConnection();
            connect.setRequestMethod("DELETE");
            String authentication = "token " + currentSessionData.getToken();
            connect.setRequestProperty("Authorization", authentication);
            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            if (responseCode == 204) {
                return true;
            }
        } catch (IOException e) {
            Log.v(TAG, "", e);
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = activity.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        SharedPreferences prefs = activity.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        SharedPreferences.Editor e = prefs.edit();
        e.remove("userSessionData");
        e.apply();
        Intent intent = new Intent(activity, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

        activity.finish();
    }
}