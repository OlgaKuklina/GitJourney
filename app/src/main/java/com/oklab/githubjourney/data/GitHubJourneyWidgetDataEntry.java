package com.oklab.githubjourney.data;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;


public class GitHubJourneyWidgetDataEntry implements Parcelable {

    private  final String authorName;
    private  final Uri avatar;
    private  final String title;
    private  final String description;
    private  final String date;

    public GitHubJourneyWidgetDataEntry(String authorName, Uri avatar, String title, String description, String date) {
        this.authorName = authorName;
        this.avatar = avatar;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    protected GitHubJourneyWidgetDataEntry(Parcel in) {
        authorName = in.readString();
        avatar = Uri.parse((in.readString()));
        title = in.readString();
        description = in.readString();
        date = in.readString();
    }

    public static final Creator<GitHubJourneyWidgetDataEntry> CREATOR = new Creator<GitHubJourneyWidgetDataEntry>() {
        @Override
        public GitHubJourneyWidgetDataEntry createFromParcel(Parcel in) {
            return new GitHubJourneyWidgetDataEntry(in);
        }

        @Override
        public GitHubJourneyWidgetDataEntry[] newArray(int size) {
            return new GitHubJourneyWidgetDataEntry[size];
        }
    };

    public Uri getAvatar() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(authorName);
        parcel.writeString(String.valueOf(avatar));
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
    }
}
