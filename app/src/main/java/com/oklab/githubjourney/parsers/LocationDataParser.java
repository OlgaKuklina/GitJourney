package com.oklab.githubjourney.parsers;
import android.util.Log;
import com.oklab.githubjourney.data.GitHubUserLocationDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-03-24.
 */

public class LocationDataParser {
    private static final String TAG = LocationDataParser.class.getSimpleName();

    public List<GitHubUserLocationDataEntry> parse(JSONArray jsonArray) throws JSONException {

        List<GitHubUserLocationDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            GitHubUserLocationDataEntry entry = parseItem(jsonArray.getJSONObject(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
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
