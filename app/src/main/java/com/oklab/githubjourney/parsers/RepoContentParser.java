package com.oklab.githubjourney.parsers;

import android.util.Log;

import com.oklab.githubjourney.data.GitHubRepoContentType;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-04-29.
 */

public class RepoContentParser {
    private static final String TAG = RepoContentParser.class.getSimpleName();

    public List<RepositoryContentDataEntry> parse(JSONArray jsonArray) throws JSONException {
        Log.v(TAG, "jsonArray " + jsonArray.length());
        List<RepositoryContentDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            RepositoryContentDataEntry entry = parseItem(jsonArray.getJSONObject(i));
            Log.v(TAG, "entry " + entry);
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }

    private RepositoryContentDataEntry parseItem(JSONObject object) throws JSONException {
        if (object == null) {
            return null;
        }
        GitHubRepoContentType type = GitHubRepoContentType.DIR;
        if (!object.getString("type").isEmpty()) {
            type = GitHubRepoContentType.getRepoContentType(object.getString("type"));
        }
        String uri = " ";
        if (!object.getString("download_url").isEmpty()) {
            uri = object.getString("download_url");
        }
        String name = " ";
        if (!object.getString("name").isEmpty()) {
            name = object.getString("name");
        }

        String path = " ";
        if (!object.getString("path").isEmpty()) {
            path = object.getString("path");
        }
        return new RepositoryContentDataEntry(name, uri, type, path);
    }
}
