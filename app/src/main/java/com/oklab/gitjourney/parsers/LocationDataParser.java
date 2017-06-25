package com.oklab.gitjourney.parsers;

import android.util.Log;

import com.oklab.gitjourney.data.GitHubUserLocationDataEntry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olgakuklina on 2017-03-24.
 */

public class LocationDataParser implements Parser<GitHubUserLocationDataEntry> {
    private static final String TAG = LocationDataParser.class.getSimpleName();

    @Override
    public GitHubUserLocationDataEntry parse(JSONObject jsonObject) throws JSONException {
        GitHubUserLocationDataEntry entry = parseItem(jsonObject);
        return entry;
    }

    private GitHubUserLocationDataEntry parseItem(JSONObject object) throws JSONException {
        if (object == null) {
            return null;
        }
        Log.v(TAG, "login = " + object.getString("login"));
        Log.v(TAG, "JSONObject = " + object);
        String login = " ";
        if (!object.getString("login").isEmpty()) {
            login = object.getString("login");
        }
        String avatarUrl = " ";
        if (!object.getString("avatar_url").isEmpty()) {
            avatarUrl = object.getString("avatar_url");
        }
        String profileUri = " ";
        if (!object.getString("url").isEmpty()) {
            profileUri = object.getString("url");
        }
        Log.v(TAG, "location = " + object.getString("location"));
        Log.v(TAG, "JSONObject = " + object);
        String location = " ";
        if (!object.getString("location").isEmpty()) {
            location = object.getString("location");
        }

        return new GitHubUserLocationDataEntry(login, avatarUrl, profileUri, location);
    }
}
