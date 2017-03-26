package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class GitHubUsersDataEntry {
    private final String login;
    private final String imageUri;
    private final String profileUri;

    public GitHubUsersDataEntry(String name, String imageUri, String profileUri) {
        this.login = name;
        this.imageUri = imageUri;
        this.profileUri = profileUri;
    }

    public String getLogin() {
        return login;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getProfileUri() {
        return profileUri;
    }
}
