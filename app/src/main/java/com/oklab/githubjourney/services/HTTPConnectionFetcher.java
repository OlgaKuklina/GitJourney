package com.oklab.githubjourney.services;

import android.util.Log;

import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.UserSessionData;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by olgakuklina on 2017-03-22.
 */

public class HTTPConnectionFetcher {
    private static final String TAG = HTTPConnectionFetcher.class.getSimpleName();
    private final String uri;
    private final UserSessionData currentSessionData;

    public HTTPConnectionFetcher(String uri, UserSessionData currentSessionData) {
        this.uri = uri;
        this.currentSessionData = currentSessionData;
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
