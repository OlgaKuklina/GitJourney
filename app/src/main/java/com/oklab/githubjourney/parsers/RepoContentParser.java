package com.oklab.githubjourney.parsers;

import com.oklab.githubjourney.data.RepositoryContentDataEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-04-29.
 */

public class RepoContentParser {
    private static final String TAG = RepoContentParser.class.getSimpleName();

    public List<RepositoryContentDataEntry> parse(JSONArray jsonArray) throws JSONException {
        List<RepositoryContentDataEntry> list = new ArrayList<RepositoryContentDataEntry>(1);
        return list;
    }
}
