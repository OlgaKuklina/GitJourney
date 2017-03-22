package com.oklab.githubjourney.data;

import android.net.Uri;
import android.os.Parcel;

/**
 * Created by olgakuklina on 2017-03-21.
 */

public class GitHubUserLocationDataEntry extends GitHubUsersDataEntry {
    private final String Location;

    public GitHubUserLocationDataEntry(String name, String imageUri, String profileUri, String location) {
        super(name, imageUri, profileUri);
        Location = location;
    }

    public String getLocation() {
        return Location;
    }
}
