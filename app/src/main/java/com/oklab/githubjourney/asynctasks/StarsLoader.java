package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.StarsDataEntry;
import com.oklab.githubjourney.parsers.StarsParser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by olgakuklina on 2017-03-30.
 */

public class StarsLoader extends AsyncTaskLoader<List<StarsDataEntry>> {
    private static final String TAG = StarsLoader.class.getSimpleName();
    private final int page;

    public StarsLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    public List<StarsDataEntry> loadInBackground() {
        String uri = getContext().getString(R.string.url_starred, page);
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());

        try {
            JSONArray jsonArray = new JSONArray(result.getResult());
            return new StarsParser().parse(jsonArray);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}
