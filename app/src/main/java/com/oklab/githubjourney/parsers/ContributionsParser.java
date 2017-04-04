package com.oklab.githubjourney.parsers;

import android.content.Context;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.ActionType;
import com.oklab.githubjourney.data.ContributionDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by olgakuklina on 2017-02-28.
 */

public class ContributionsParser {

    private static final String TAG = ContributionsParser.class.getSimpleName();
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private final Context context;

    public ContributionsParser(Context context) {
        this.context = context;
    }

    public List<ContributionDataEntry> parse(JSONArray jsonArray) throws JSONException {

        List<ContributionDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ContributionDataEntry entry = parseItem(jsonArray.getJSONObject(i));
                if (entry != null) {
                    dataEntriesList.add(entry);
                } else {
                    Log.w(TAG, "ContributionDataEntry is empty");
                }
            } catch (ParseException ex) {
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
        } else {
            Log.e(TAG, "Failed to obtain entry id");
            return null;
        }

        ActionType actionType;
        if (!object.getString("type").isEmpty()) {
            actionType = ActionType.getFeedType(object.getString("type"));
        } else {
            Log.e(TAG, "Failed to obtain actionType data for entry id " + entryId);
            return null;
        }
        String title;
        if (!object.getString("type").isEmpty()) {
            title = object.getString("type");
        } else {
            title = "unknown activity event type";
        }
        String desc = null;
        if (object.getJSONObject("repo") != null) {
            JSONObject object1 = object.getJSONObject("repo");

            if (!object1.getString("name").isEmpty()) {
                desc = object1.getString("name");
                if (!object1.getString("url").isEmpty()) {
                    desc = context.getString(R.string.contribution_description, desc, object1.getString("url"));
                }
            }
        }
        String entryURL = getUri(actionType, object);

        Calendar contributionDate = null;
        if (!object.getString("created_at").isEmpty()) {
            String date = object.getString("created_at");
            SimpleDateFormat format = new SimpleDateFormat(PATTERN, Locale.getDefault());
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date entryDate = format.parse(date);
            contributionDate = Calendar.getInstance();
            contributionDate.setTime(entryDate);
        }
        return new ContributionDataEntry(entryId, entryURL, title, desc, actionType, contributionDate);
    }

    private String getUri(ActionType type, JSONObject obj) throws JSONException {
        if (type == ActionType.PUSH) {
            return obj.getJSONObject("payload").getJSONArray("commits").getJSONObject(0).getString("url");
        }
        return null;

    }
}
