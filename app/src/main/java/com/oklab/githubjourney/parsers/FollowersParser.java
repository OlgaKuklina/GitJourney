package com.oklab.githubjourney.parsers;

import android.util.Log;

import com.oklab.githubjourney.data.GitHubUsersDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class FollowersParser {

    private static final String TAG = FollowersParser.class.getSimpleName();

    public List<GitHubUsersDataEntry> parse(JSONArray jsonArray) throws JSONException {

        List<GitHubUsersDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            GitHubUsersDataEntry entry = parseItem(jsonArray.getJSONObject(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }

    private GitHubUsersDataEntry parseItem(JSONObject object) throws JSONException {
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

        return new GitHubUsersDataEntry(login, avatarUrl, profileUri);
    }
}
