package com.oklab.gitjourney.data;

import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-02-05.
 */

public class UserActivityDataEntry {

    private final long entryId;
    private final String title;
    private final String description;
    private final ActionType actionType;
    private final Calendar date;

    public UserActivityDataEntry(long entryId, String title, String description, ActionType actionType, Calendar date) {
        this.entryId = entryId;
        this.title = title;
        this.description = description;
        this.actionType = actionType;
        this.date = date;
    }

    public long getEntryId() {
        return entryId;
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
