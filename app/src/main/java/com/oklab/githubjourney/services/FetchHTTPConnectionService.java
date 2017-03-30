package com.oklab.githubjourney.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.utils.Utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by olgakuklina on 2017-03-22.
 */

public class FetchHTTPConnectionService {
    private static final String TAG = FetchHTTPConnectionService.class.getSimpleName();
    private final String uri;
    private final UserSessionData currentSessionData;

    public FetchHTTPConnectionService(String uri, UserSessionData currentSessionData) {
        this.uri = uri;
        this.currentSessionData = currentSessionData;
    }

    public FetchHTTPConnectionService(String uri, Context context) {
        this.uri = uri;
        SharedPreferences prefs = context.getSharedPreferences(Utils.SHARED_PREF_NAME, 0);
        String sessionDataStr = prefs.getString("userSessionData", null);
        currentSessionData = UserSessionData.createUserSessionDataFromString(sessionDataStr);
    }

    public HTTPConnectionResult establishConnection() {
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(uri).openConnection();
            connect.setRequestMethod("GET");

            String authentication = "token " + currentSessionData.getToken();
            connect.setRequestProperty("Authorization", authentication);

            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            return new HTTPConnectionResult(response, responseCode);

        } catch (Exception e) {
            Log.e(TAG, "Get data failed", e);
            return null;
        }
    }
}
