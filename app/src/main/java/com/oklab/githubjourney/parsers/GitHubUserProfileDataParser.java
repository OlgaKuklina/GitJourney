package com.oklab.githubjourney.parsers;

import android.util.Log;

import com.oklab.githubjourney.data.GitHubUserLocationDataEntry;
import com.oklab.githubjourney.data.GitHubUserProfileDataEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public class GitHubUserProfileDataParser {
    private static final String TAG = GitHubUserProfileDataParser.class.getSimpleName();
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public GitHubUserProfileDataEntry parse(JSONObject jsonObject) throws JSONException {
        GitHubUserProfileDataEntry entry = parseItem(jsonObject);
        return entry;
    }

    private GitHubUserProfileDataEntry parseItem(JSONObject object) throws JSONException {
        if (object == null) {
            return null;
        }
        Log.v(TAG, "login = " + object.getString("login"));
        Log.v(TAG, "JSONObject = " + object);
        String login = "";
        if (!object.getString("login").isEmpty()) {
            login = object.getString("login");
        }
        String avatarUrl = "";
        if (!object.getString("avatar_url").isEmpty()) {
            avatarUrl = object.getString("avatar_url");
        }
        String profileUri = "";
        if (!object.getString("url").isEmpty()) {
            profileUri = object.getString("url");
        }
        Log.v(TAG, "location = " + object.getString("location"));
        Log.v(TAG, "JSONObject = " + object);
        String location = "";
        if (!object.getString("location").isEmpty()) {
            location = object.getString("location");
        }
        String name = "";
        if (!object.getString("name").isEmpty()) {
            name = object.getString("name");
        }
        String company = "";
        if (!object.getString("company").isEmpty()) {
            company = object.getString("company");
        }
        String blogURI = "";
        if (!object.getString("blog").isEmpty()) {
            blogURI = object.getString("blog");
        }
        String email = "";
        if (!object.getString("email").isEmpty()) {
            email = object.getString("email");
        }
        String bio = "";
        if (!object.getString("bio").isEmpty()) {
            bio = object.getString("bio");
        }
        int publicRepos = 0;
        if (!object.has("public_repos")) {
            publicRepos = object.getInt("public_repos");
        }
        int publicGists = 0;
        if (!object.has("public_gists")) {
            publicGists = object.getInt("public_gists");
        }
        int followers = 0;
        if (!object.has("followers")) {
            followers = object.getInt("followers");
        }
        int following = 0;
        if (!object.has("following")) {
            following = object.getInt("following");
        }

        Calendar createdAt = null;
        if (!object.getString("created_at").isEmpty()) {
            String date = object.getString("created_at");
            SimpleDateFormat format = new SimpleDateFormat(PATTERN);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date entryDate = null;
            try {
                entryDate = format.parse(date);
                createdAt = Calendar.getInstance();
                createdAt.setTime(entryDate);
            } catch (ParseException e) {
                Log.v(TAG, "", e);
            }
        }
        return new GitHubUserProfileDataEntry(name, avatarUrl, profileUri, location, login, company, blogURI, email, bio, publicRepos, publicGists, followers, following, createdAt);
        }
}
