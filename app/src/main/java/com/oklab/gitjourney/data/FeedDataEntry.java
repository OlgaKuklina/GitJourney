package com.oklab.gitjourney.data;

import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-01-15.
 */

public class FeedDataEntry {
    private final long entryId;
    private final String authorName;
    private final String avatarURL;
    private final String authorURL;
    private final String title;
    private final String description;
    private final ActionType actionType;
    private final Calendar date;

    public FeedDataEntry(long entryId, String authorName, String avatarURL, String authorURL, String title, String description, ActionType actionType, Calendar date) {
        this.entryId = entryId;
        this.authorName = authorName;
        this.avatarURL = avatarURL;
        this.authorURL = authorURL;
        this.title = title;
        this.description = description;
        this.actionType = actionType;
        this.date = date;
    }

    public long getEntryId() {
        return entryId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Calendar getDate() {
        return date;
    }

    public String getAuthorURL() {
        return authorURL;
    }
}
