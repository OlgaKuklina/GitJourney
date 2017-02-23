package com.oklab.githubjourney.data;


public class GitHubJourneyWidgetDataEntry {

    private final String authorName;
    private final String avatar;
    private final String title;
    private final String description;
    private final String date;



    public GitHubJourneyWidgetDataEntry(String authorName, String avatar, String title, String description, String date) {
        this.authorName = authorName;
        this.avatar = avatar;
        this.title = title;
        this.description = description;
        this.date = date;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
