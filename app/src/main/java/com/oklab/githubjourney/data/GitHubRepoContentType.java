package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-04-19.
 */

public enum GitHubRepoContentType {
    EMPTY("empty", 0),
    SUBMODULE("submodule", 1),
    DIR("dir", 2),
    FILE("file", 3),
    SYMLINK("symlink", 4);


    private final String matchingRepoContentType;
    private final int priority;

    GitHubRepoContentType(String matchingRepoContentType, int priority) {
        this.matchingRepoContentType = matchingRepoContentType;
        this.priority = priority;
    }

    public static GitHubRepoContentType getRepoContentType(String repoContentType) {
        for (GitHubRepoContentType type : values()) {
            if (type.matchingRepoContentType.equals(repoContentType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown repo content type: " + repoContentType);
    }

    public int getPriority() {
        return priority;
    }
}
