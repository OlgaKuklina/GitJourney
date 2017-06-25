package com.oklab.gitjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.data.ContributionDataEntry;
import com.oklab.gitjourney.data.UserSessionData;
import com.oklab.gitjourney.parsers.ContributionsParser;
import com.oklab.gitjourney.utils.Utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-28.
 */

public class ContributionsAsyncTask extends AsyncTask<Integer, Void, List<ContributionDataEntry>> {
    private static final String TAG = ContributionsAsyncTask.class.getSimpleName();
    private final Context context;
    private UserSessionData currentSessionData;
    private ContributionsAsyncTask.OnContributionsLoadedListener listener;

    public ContributionsAsyncTask(Context context, ContributionsAsyncTask.OnContributionsLoadedListener listener) {
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
    protected List<ContributionDataEntry> doInBackground(Integer... args) {
        Integer page = args[0];
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(context.getString(R.string.url_events, page, currentSessionData.getLogin())).openConnection();
            connect.setRequestMethod("GET");
            String authentication = "token " + currentSessionData.getToken();
            connect.setRequestProperty("Authorization", authentication);

            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            JSONArray jsonArray = new JSONArray(response);

            return new ContributionsParser(context).parse(jsonArray);

        } catch (Exception e) {
            Log.e(TAG, "Get contributions failed", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ContributionDataEntry> entryList) {
        super.onPostExecute(entryList);
        listener.onContributionsLoaded(entryList);
    }

    public interface OnContributionsLoadedListener<ContributionDataEntry> {
        void onContributionsLoaded(List<ContributionDataEntry> contributionsDataEntry);
    }
}
