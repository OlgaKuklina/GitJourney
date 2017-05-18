package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;


/**
 * Created by olgakuklina on 5/17/17.
 */

public class RepositoryFileContentLoader extends AsyncTaskLoader<String> {
    private static final String TAG = RepositoryFileContentLoader.class.getSimpleName();
    private final String downloadURI;

    public RepositoryFileContentLoader(Context context, String downloadURI) {
        super(context);
        this.downloadURI = downloadURI;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(downloadURI, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());

        if (result.getResponceCode() == 200) {
            return result.getResult();
        }
        return null;
    }
}