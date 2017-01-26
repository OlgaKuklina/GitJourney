package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class GitHubUsersDataEntry {
    private  final String name;
    private  final String imageUri;
    private  final String profileUri;

    public GitHubUsersDataEntry(String name, String imageUri, String profileUri) {
        this.name = name;
        this.imageUri = imageUri;
        this.profileUri = profileUri;
    }

    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getProfileUri() {
        return profileUri;
    }
}
