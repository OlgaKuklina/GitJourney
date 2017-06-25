package com.oklab.gitjourney.data;

/**
 * Created by olgakuklina on 2017-01-15.
 */

public enum ActionType {
    FORK("ForkEvent"),
    ISSUE("IssuesEvent"),
    PULL_REQUEST("PullRequestEvent"),
    COMMENT("IssueCommentEvent"),
    STAR("WatchEvent"),
    DELETE("DeleteEvent"),
    CREATE("CreateEvent"),
    PUSH("PushEvent"),
    COMMIT_COMMENT("CommitCommentEvent");

    private final String matchingEventType;

    ActionType(String matchingEventType) {
        this.matchingEventType = matchingEventType;
    }

    public static ActionType getFeedType(String eventType) {
        for (ActionType action : values()) {
            if (action.matchingEventType.equals(eventType)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + eventType);
    }
}
