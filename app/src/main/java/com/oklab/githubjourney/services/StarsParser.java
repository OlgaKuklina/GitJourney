package com.oklab.githubjourney.services;

import com.oklab.githubjourney.data.ReposDataEntry;
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
        for(int i = 0; i < jsonArray.length(); i++) {
            StarsDataEntry entry = parseItem(jsonArray.getJSONObject(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }
    private StarsDataEntry parseItem(JSONObject object) throws JSONException {
        return null;
    }
}
