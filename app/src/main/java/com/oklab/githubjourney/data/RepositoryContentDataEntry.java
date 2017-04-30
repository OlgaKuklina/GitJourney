package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-04-18.
 */

public class RepositoryContentDataEntry {
    private final String name;
    private final String uri;
    private final GitHubRepoContentType type;
    private final String path;

    public RepositoryContentDataEntry(String name, String uri, GitHubRepoContentType type, String path) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.path = path;
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

    public String getPath() {
        return path;
    }
}
