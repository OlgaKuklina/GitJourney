package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class ReposDataEntry {

    private final String title;
    private final boolean privacy;
    private final boolean forked;
    private final String description;
    private final String language;
    private final int stars;
    private final int forks;

    public ReposDataEntry(String title, boolean privacy, boolean forked, String description, String language, int stars, int forks) {
        this.title = title;
        this.privacy = privacy;
        this.forked = forked;
        this.description = description;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
    }

    public boolean isForked() {
        return forked;
    }

    public boolean isPrivate() {
        return privacy;
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
