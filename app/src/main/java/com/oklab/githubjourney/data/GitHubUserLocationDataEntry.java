package com.oklab.githubjourney.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by olgakuklina on 2017-03-21.
 */

public class GitHubUserLocationDataEntry extends GitHubUsersDataEntry implements Parcelable {
    private final String location;
    private double latitude;
    private double longitude;



    public GitHubUserLocationDataEntry(String name, String imageUri, String profileUri, String location, double latitude, double longitude) {
        super(name, imageUri, profileUri);
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public GitHubUserLocationDataEntry(String name, String imageUri, String profileUri, String location) {
        this(name, imageUri, profileUri, location, 0.0, 0.0);
    }

    public static final Creator<GitHubUserLocationDataEntry> CREATOR = new Creator<GitHubUserLocationDataEntry>() {
        @Override
        public GitHubUserLocationDataEntry createFromParcel(Parcel in) {
            String location = in.readString();
            String name = in.readString();
            String imageUri = in.readString();
            String profileUri = in.readString();
            double latitude = in.readDouble();
            double longitude = in.readDouble();

            return new GitHubUserLocationDataEntry(name, imageUri, profileUri, location);
        }

        @Override
        public GitHubUserLocationDataEntry[] newArray(int size) {
            return new GitHubUserLocationDataEntry[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
        parcel.writeString(getName());
        parcel.writeString(getImageUri());
        parcel.writeString(getProfileUri());
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

}
