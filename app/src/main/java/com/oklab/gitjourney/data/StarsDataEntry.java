package com.oklab.gitjourney.data;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class StarsDataEntry {

    private final String title;
    private final String login;
    private final String fullName;
    private final String description;
    private final String language;
    private final boolean privacy;
    private final int watchers;
    private final int stars;
    private final int forks;

    public StarsDataEntry(String title, String login, String fullName, String language, String description, boolean privacy, int watchers, int forks, int stars) {
        this.title = title;
        this.login = login;
        this.description = description;
        this.language = language;
        this.fullName = fullName;
        this.privacy = privacy;
        this.watchers = watchers;
        this.stars = stars;
        this.forks = forks;
    }

    public String getLogin() {
        return login;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isPrivate() {
        return privacy;
    }

    public int getWatchers() {
        return watchers;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public int getStars() {
        return stars;
    }

    public int getForks() {
        return forks;
    }
}
