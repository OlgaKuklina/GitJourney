package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-04-19.
 */

public enum GitHubRepoContentType {
    FILE("file"),
    DIR("dir"),
    SYMLINK("symlink"),
    SUBMODULE("submodule");

    private final String matchingRepoContentType;

    GitHubRepoContentType(String matchingRepoContentType) {
        this.matchingRepoContentType = matchingRepoContentType;
    }

    public static GitHubRepoContentType getRepoContentType(String repoContentType) {
        for (GitHubRepoContentType type : values()) {
            if (type.matchingRepoContentType.equals(repoContentType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown repo content type: " + repoContentType);
    }
}
