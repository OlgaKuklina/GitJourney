package com.oklab.githubjourney.services;

import android.util.Log;

import com.oklab.githubjourney.data.ActionType;
import com.oklab.githubjourney.data.ContributionDataEntry;
import com.oklab.githubjourney.data.GitHubUsersDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by olgakuklina on 2017-02-28.
 */

public class ContributionsParser {

    private static final String TAG = ContributionsParser.class.getSimpleName();
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public List<ContributionDataEntry> parse(JSONArray jsonArray) throws JSONException {

        List<ContributionDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try{
                ContributionDataEntry entry = parseItem(jsonArray.getJSONObject(i));
                if(entry!=null) {
                    dataEntriesList.add(entry);
                }
                else {
                    Log.w(TAG, "ContributionDataEntry is empty");
                }
            }
            catch (ParseException ex) {
                Log.e(TAG, "", ex);
            }
        }
        return dataEntriesList;
    }

    private ContributionDataEntry parseItem(JSONObject object) throws JSONException, ParseException {
        if (object == null) {
            return null;
        }
        Log.v(TAG, "JSONObject = " + object);
        long entryId;
        if (!object.getString("id").isEmpty()) {
            entryId = Long.parseLong(object.getString("id"));
        }
        else{
            Log.e(TAG, "Failed to obtain entry id");
            return null;
        }

        ActionType actionType;
        if (!object.getString("type").isEmpty()) {
            actionType = ActionType.getFeedType(object.getString("type"));
        }
        else {
            Log.e(TAG, "Failed to obtain actionType data for entry id " + entryId);
            return null;
        }
        String entryURL = getUri(actionType, object);

        Calendar contributionDate = null;
        if (!object.getString("date").isEmpty()) {
            String date = object.getString("date");
            Date entryDate = new SimpleDateFormat(PATTERN).parse(date);
            contributionDate = Calendar.getInstance();
            contributionDate.setTime(entryDate);
        }
        return new ContributionDataEntry(entryId, entryURL, null, null, actionType, contributionDate);
    }
    private String getUri(ActionType type, JSONObject obj) throws JSONException {
        if(type == ActionType.PUSH) {
            return obj.getJSONObject("payload").getJSONArray("commits").getJSONObject(0).getString("url");
        }
        return null;

    }
}
