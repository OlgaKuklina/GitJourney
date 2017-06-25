package com.oklab.gitjourney.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class GitHubUsersDataEntry implements Parcelable {

    public static final Creator<GitHubUsersDataEntry> CREATOR = new Creator<GitHubUsersDataEntry>() {
        @Override
        public GitHubUsersDataEntry createFromParcel(Parcel in) {
            return new GitHubUsersDataEntry(in);
        }

        @Override
        public GitHubUsersDataEntry[] newArray(int size) {
            return new GitHubUsersDataEntry[size];
        }
    };
    private final String login;
    private final String imageUri;
    private final String profileUri;

    public GitHubUsersDataEntry(String name, String imageUri, String profileUri) {
        this.login = name;
        this.imageUri = imageUri;
        this.profileUri = profileUri;
    }

    protected GitHubUsersDataEntry(Parcel in) {
        login = in.readString();
        imageUri = in.readString();
        profileUri = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(login);
        parcel.writeString(imageUri);
        parcel.writeString(profileUri);
    }
}
