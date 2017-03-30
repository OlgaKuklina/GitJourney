package com.oklab.githubjourney.asynctasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.UserSessionData;
import com.oklab.githubjourney.parsers.Parser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;
import com.oklab.githubjourney.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olgakuklina on 2017-03-29.
 */

public class UserProfileLoader<T> extends AsyncTaskLoader<T>  {

    private static final String TAG = UserProfileLoader.class.getSimpleName();
    private final Parser<T> parser;
    private final String login;

    public UserProfileLoader(Context context, Parser<T> parser, String login) {
        super(context);
        this.parser = parser;
        this.login = login;
    }

    @Override
    public T loadInBackground() {
        String uri = getContext().getString(R.string.url_users, login);
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "response = " + result.getResult());

        try {
            JSONObject jsonObject = new JSONObject(result.getResult());
            return parser.parse(jsonObject);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}
