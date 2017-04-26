package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-04-18.
 */

public class RepositoryContentDataEntry {
    private final String name;
    private final String uri;
    private final GitHubRepoContentType type;

    public RepositoryContentDataEntry(String name, String uri, GitHubRepoContentType type) {
        this.name = name;
        this.uri = uri;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public GitHubRepoContentType getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }
}
