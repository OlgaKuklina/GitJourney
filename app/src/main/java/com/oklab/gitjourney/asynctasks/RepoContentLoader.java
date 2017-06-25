package com.oklab.gitjourney.asynctasks;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.oklab.gitjourney.R;
import com.oklab.gitjourney.data.HTTPConnectionResult;
import com.oklab.gitjourney.data.RepositoryContentDataEntry;
import com.oklab.gitjourney.parsers.RepoContentParser;
import com.oklab.gitjourney.services.FetchHTTPConnectionService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


/**
 * Created by olgakuklina on 2017-04-17.
 */

public class RepoContentLoader extends AsyncTaskLoader<List<RepositoryContentDataEntry>> {
    private static final String TAG = RepoContentLoader.class.getSimpleName();
    private final String path;
    private final String repoName;
    private final String userName;

    public RepoContentLoader(Context context, String path, String repoName, String userName) {
        super(context);

        String[] pathList = path.split("/");
        for (int i = 0; i < pathList.length; i++) {
            pathList[i] = Uri.encode(pathList[i]);
        }
        this.path = TextUtils.join("/", pathList);
        this.repoName = repoName;
        this.userName = userName;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<RepositoryContentDataEntry> loadInBackground() {
        String uri = getContext().getString(R.string.url_repo_content, userName, repoName, path);
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());

        try {
            JSONArray jsonArray = new JSONArray(result.getResult());
            return new RepoContentParser().parse(jsonArray);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}