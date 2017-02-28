package com.oklab.githubjourney.data;

import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-02-28.
 */

public class ContributionDataEntry {
    private final long entryId;
    private final String avatarURL;
    private final String entryURL;
    private final String title;
    private final String description;
    private final ActionType actionType;
    private final Calendar date;

    public ContributionDataEntry(long entryId, String avatarURL, String entryURL, String title, String description, ActionType actionType, Calendar date) {
        this.entryId = entryId;
        this.avatarURL = avatarURL;
        this.entryURL = entryURL;
        this.title = title;
        this.description = description;
        this.actionType = actionType;
        this.date = date;
    }

    public long getEntryId() {
        return entryId;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getEntryURL() {
        return entryURL;
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
}
