package com.oklab.githubjourney.asynctasks;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.parsers.RepoReadmeParser;
import com.oklab.githubjourney.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by olgakuklina on 2017-04-17.
 */

public class RepoReadmeAsyncTask <T> extends AsyncTask<String, Void, List<RepositoryContentDataEntry>> {
    private static final String TAG = RepoReadmeAsyncTask.class.getSimpleName();
    private final Context context;
    private final onRepoReadmeLoadedListener listener;
    private UserSessionData currentSessionData;

    public RepoReadmeAsyncTask(Context context, onRepoReadmeLoadedListener listener) {
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
    protected List<RepositoryContentDataEntry> doInBackground(String... args) {
        String repo = args[0];
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_repo_readme, currentSessionData.getLogin(), repo)).openConnection();
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
            try {
                JSONObject jsonObj = new JSONObject(response);
                return new RepoReadmeParser().parse(jsonObj);

            } catch (JSONException e) {
                Log.e(TAG, "", e);
            }
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<RepositoryContentDataEntry> entryList) {
        super.onPostExecute(entryList);
        listener.onRepoReadmeLoaded(entryList);
    }

    public interface onRepoReadmeLoadedListener<T> {
        void onRepoReadmeLoaded(List<RepositoryContentDataEntry> readmeDataEntry);
    }
}