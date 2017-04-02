package com.oklab.githubjourney.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public class GitHubUserProfileDataEntry extends GitHubUsersDataEntry implements Parcelable {

    public static final Creator<GitHubUserProfileDataEntry> CREATOR = new Creator<GitHubUserProfileDataEntry>() {
        @Override
        public GitHubUserProfileDataEntry createFromParcel(Parcel in) {
            return new GitHubUserProfileDataEntry(in);
        }

        @Override
        public GitHubUserProfileDataEntry[] newArray(int size) {
            return new GitHubUserProfileDataEntry[size];
        }
    };
    private final String location;
    private final String name;
    private final String company;
    private final String blogURI;
    private final String email;
    private final String bio;
    private final int publicRepos;
    private final int publicGists;
    private final int followers;
    private final int following;
    private final Calendar createdAt;

    public GitHubUserProfileDataEntry(String name, String imageUri, String profileUri, String location, String login, String company, String blogURI, String email, String bio, int publicRepos, int publicGists, int followers, int following, Calendar createdAt) {
        super(login, imageUri, profileUri);
        this.location = location;
        this.name = name;
        this.company = company;
        this.email = email;
        this.blogURI = blogURI;
        this.bio = bio;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.createdAt = createdAt;
    }

    protected GitHubUserProfileDataEntry(Parcel in) {
        super(in);
        location = in.readString();
        name = in.readString();
        company = in.readString();
        email = in.readString();
        blogURI = in.readString();
        bio = in.readString();
        publicRepos = in.readInt();
        publicGists = in.readInt();
        followers = in.readInt();
        following = in.readInt();
        long date = in.readLong();
        String timeZone = in.readString();
        createdAt = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        createdAt.setTimeInMillis(date);
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getBlogURI() {
        return blogURI;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public int getPublicGists() {
        return publicGists;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(location);
        parcel.writeString(name);
        parcel.writeString(company);
        parcel.writeString(email);
        parcel.writeString(blogURI);
        parcel.writeString(bio);
        parcel.writeInt(publicRepos);
        parcel.writeInt(publicGists);
        parcel.writeInt(followers);
        parcel.writeInt(following);
        parcel.writeLong(createdAt.getTimeInMillis());
        parcel.writeString(createdAt.getTimeZone().getID());
    }
}
