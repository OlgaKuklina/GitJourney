package com.oklab.gitjourney.asynctasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.adapters.FirebaseAnalyticsWrapper;
import com.oklab.gitjourney.data.HTTPConnectionResult;
import com.oklab.gitjourney.data.RepositoryContentDataEntry;
import com.oklab.gitjourney.parsers.RepoContentParser;
import com.oklab.gitjourney.services.FetchHTTPConnectionService;
import com.oklab.gitjourney.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


/**
 * Created by olgakuklina on 2017-04-17.
 */

public class RepoContentLoader extends AsyncTaskLoader<List<RepositoryContentDataEntry>> {
    private static final String TAG = RepoContentLoader.class.getSimpleName();
    private static final String fbAEvent = "Parser failed Stack Trace";
    private final String path;
    private final String repoName;
    private final String userName;
    private boolean isRepoReady = true;
    private FirebaseAnalyticsWrapper firebaseAnalytics;

    public RepoContentLoader(Context context, String path, String repoName, String userName) {
        super(context);

        if (repoName == null || userName == null || repoName.isEmpty() || userName.isEmpty()) {
            isRepoReady = false;
        }

        String[] pathList = path.split("/");
        for (int i = 0; i < pathList.length; i++) {
            pathList[i] = Uri.encode(pathList[i]);
        }
        this.path = TextUtils.join("/", pathList);
        this.repoName = repoName;
        this.userName = userName;
        firebaseAnalytics = new FirebaseAnalyticsWrapper(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<RepositoryContentDataEntry> loadInBackground() {
        if (isRepoReady) {
            String uri = getContext().getString(R.string.url_repo_content, userName, repoName, path);
            FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
            HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
            if (result == null) {
                return null;
            }

            try {
                JSONArray jsonArray = new JSONArray(result.getResult());
                return new RepoContentParser().parse(jsonArray);

            } catch (JSONException e) {
                Log.e(TAG, "Parse Error  ", e);
                Bundle bundle = new Bundle();
                bundle.putString(TAG, Utils.getStackTrace(e));
                firebaseAnalytics.logEvent(fbAEvent, bundle);
            }
        }
        return null;
    }
}