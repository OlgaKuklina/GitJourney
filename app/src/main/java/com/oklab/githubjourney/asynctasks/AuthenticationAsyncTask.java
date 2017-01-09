package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.oklab.githubjourney.githubjourney.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olgakuklina on 2017-01-08.
 */

public class AuthenticationAsyncTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = AuthenticationAsyncTask.class.getSimpleName();

    private final Context context;

    public AuthenticationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.uri_connect)).openConnection();
            connect.setRequestMethod("POST");
            connect.setDoOutput(true);
            String inputString = args[0] + ":" + args[1];
            String authentication  = "basic " + Base64.encodeToString(inputString.getBytes(), Base64.NO_WRAP);
            connect.setRequestProperty("Authorization", authentication);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("client_id", context.getString(R.string.client_id));
            jsonObject.put("client_secret", context.getString(R.string.client_secret));
            jsonObject.put("fingerprint", context.getString(R.string.fingerprint));
            jsonObject.put("note", context.getString(R.string.note));
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("repo");
            jsonObject.put("scopes", jsonArray);

            OutputStream outputStream = connect.getOutputStream();
            Log.v(TAG, "request body = " + jsonObject.toString());
            outputStream.write(jsonObject.toString().getBytes());
            connect.connect();
            int responseCode = connect.getResponseCode();
            Log.v(TAG, "responseCode = " + responseCode);

        } catch (Exception e) {
            Log.e(TAG, "Login failed", e);
            return null;
        }
        return null;
    }
}
