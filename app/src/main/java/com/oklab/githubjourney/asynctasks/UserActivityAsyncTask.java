package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.oklab.githubjourney.data.UserActivityDataEntry;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.utils.Utils;

import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class UserActivityAsyncTask extends AsyncTask<Void, Void, List<UserActivityDataEntry>> {
    private static final String TAG = UserActivityAsyncTask.class.getSimpleName();
    private final Context context;
    private UserSessionData currentSessionData;
    private UserActivityAsyncTask.OnActivityLoadedListener listener;


    public UserActivityAsyncTask(Context context, OnActivityLoadedListener listener) {
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
    protected List<UserActivityDataEntry> doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(List<UserActivityDataEntry> entryList) {
        super.onPostExecute(entryList);
        listener.onActivityLoaded(entryList);
    }

    public interface OnActivityLoadedListener {
        void onActivityLoaded(List<UserActivityDataEntry> userActivityDataEntry);
    }
}
