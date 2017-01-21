package com.oklab.githubjourney.services;

import com.oklab.githubjourney.data.FeedDataEntry;
import com.oklab.githubjourney.data.ReposDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by olgakuklina on 2017-01-20.
 */

public class ReposParser {
    private static final String TAG = ReposParser.class.getSimpleName();

    public List<ReposDataEntry> parse(JSONArray jsonArray) throws JSONException {

        List<ReposDataEntry> dataEntriesList = new ArrayList<>(jsonArray.length());
        for(int i = 0; i < jsonArray.length(); i++) {
            ReposDataEntry entry = parseItem(jsonArray.getJSONObject(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }
    private ReposDataEntry parseItem(JSONObject object) throws JSONException {
        if(object == null) {
            return null;
        }
        String name =  " ";
        if(!object.getString("name").isEmpty()) {
             name = object.getString("name");
        }
        String language = " ";
        if(!object.getString("language").isEmpty()) {
             language = object.getString("language");
        }
        String description = " ";
        if (!object.getString("description").isEmpty()){
            description = object.getString("description");
        }

        boolean privacy = object.getBoolean("private");
        boolean forked = object.getBoolean("fork");
        int forksCount = object.getInt("forks_count");
        int stars = object.getInt("stargazers_count");

        return new ReposDataEntry(name, privacy, forked, description,language,stars, forksCount);
    }
}
