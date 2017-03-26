package com.oklab.githubjourney.data;

import java.util.Calendar;

/**
 * Created by olgakuklina on 2017-03-25.
 */

public class GitHubUserProfileDataEntry extends GitHubUsersDataEntry {

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
        this.blogURI = blogURI;
        this.email = email;
        this.bio = bio;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.createdAt = createdAt;
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
}
