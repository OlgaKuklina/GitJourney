package com.oklab.githubjourney.services;

import android.util.Log;

import com.oklab.githubjourney.data.StarsDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class StarsParser {
    private static final String TAG = StarsParser.class.getSimpleName();

    public List<StarsDataEntry> parse(JSONArray jsonArray) throws JSONException {

        List<StarsDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        Log.v(TAG, "jsonArray.length() = " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            StarsDataEntry entry = parseItem(jsonArray.getJSONObject(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }

    private StarsDataEntry parseItem(JSONObject object) throws JSONException {
        if (object == null) {
            return null;
        }
        Log.v(TAG, "object = " + object );
        String name = " ";
        if (!object.getString("name").isEmpty()) {
            name = object.getString("name");
        }

        String fullName = " ";
        if (!object.getString("full_name").isEmpty()) {
            fullName = object.getString("full_name");
        }
        String language = " ";
        if (!object.getString("language").isEmpty()) {
            language = object.getString("language");
        }
        String description = " ";
        if (!object.getString("description").isEmpty()) {
            description = object.getString("description");
        }
        boolean privacy = object.getBoolean("private");
        int watchersCount = object.getInt("watchers_count");
        int forksCount = object.getInt("forks_count");
        int stars = object.getInt("stargazers_count");

        return new StarsDataEntry(name, fullName, language, description, privacy, watchersCount, forksCount, stars);
    }
}
