package com.oklab.gitjourney.data;

/**
 * Created by olgakuklina on 2017-01-15.
 */

public enum ActionType {
    ISSUE("IssuesEvent"),
    COMMENT("IssueCommentEvent"),
    STAR("WatchEvent"),
    DELETE("DeleteEvent"),
    CREATE("CreateEvent"),
    COMMIT_COMMENT("CommitCommentEvent"),
    GOLLUM("GollumEvent"),
    DEPLOYMENT("DeploymentEvent"),
    DEPLOYMENT_STATUS("DeploymentStatusEvent"),
    DOWNLOAD("DownloadEvent"),
    FOLLOW("FollowEvent"),
    FORK("ForkEvent"),
    FORK_APPLY("ForkApplyEvent"),
    GIST("GistEvent"),
    INSTALLATION("InstallationEvent"),
    INSTALLATION_REPOSITORIES("InstallationRepositoriesEvent"),
    LABEL("LabelEvent"),
    MARKET_PLACE_PURCHASE("MarketplacePurchaseEvent"),
    MEMBER("MemberEvent"),
    MEMBERSHIP("MembershipEvent"),
    MILESTONE("MilestoneEvent"),
    ORGANIZATION("OrganizationEvent"),
    ORG_BLOCK("OrgBlockEvent"),
    PAGE_BUILD("PageBuildEvent"),
    PROJECT_CARD("ProjectCardEvent"),
    PROJECT_COLUMN("ProjectColumnEvent"),
    PROJECT("ProjectEvent"),
    PUBLIC("PublicEvent"),
    PULL_REQUEST("PullRequestEvent"),
    PULL_REQUEST_REVIEW("PullRequestReviewEvent"),
    PULL_REQUEST_REVIEW_COMMENT("PullRequestReviewCommentEvent"),
    PUSH("PushEvent"),
    RELEASE("ReleaseEvent"),
    REPOSITORY("RepositoryEvent"),
    STATUS("StatusEvent"),
    TEAM("TeamEvent"),
    TEAM_ADD("TeamAddEvent");




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
