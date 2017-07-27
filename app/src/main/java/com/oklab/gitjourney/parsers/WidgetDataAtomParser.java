package com.oklab.gitjourney.parsers;

import android.net.Uri;
import android.util.Log;

import com.oklab.gitjourney.data.ActionType;
import com.oklab.gitjourney.data.GitHubJourneyWidgetDataEntry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by olgakuklina on 2017-02-22.
 */

public class WidgetDataAtomParser implements AtomParser<GitHubJourneyWidgetDataEntry> {

    private static final String TAG = WidgetDataAtomParser.class.getSimpleName();
    private final DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();

    @Override
    public List<GitHubJourneyWidgetDataEntry> parse(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = dBFactory.newDocumentBuilder();
        Document xmlDoc = builder.parse(url);
        NodeList nodeList = xmlDoc.getElementsByTagName("entry");

        List<GitHubJourneyWidgetDataEntry> dataEntriesList = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            GitHubJourneyWidgetDataEntry entry = parseItem((Element) nodeList.item(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }

    private GitHubJourneyWidgetDataEntry parseItem(Element element) {
        NodeList idNodeList = element.getElementsByTagName("id");

        if (idNodeList.getLength() == 0) {
            return null;
        }
        String idContent = idNodeList.item(0).getTextContent();
        Log.v(TAG, "idContent - " + idContent);
        int index = idContent.lastIndexOf(":");
        int index2 = idContent.indexOf("/", index);

        String eventType = idContent.substring(index + 1, index2);
        ActionType actionType = getFeedType(eventType);
        String eventId = idContent.substring(index2 + 1);
        long id = Long.parseLong(eventId);

        NodeList titleNodeList = element.getElementsByTagName("title");
        String eventTitle = titleNodeList.getLength() == 0 ? " " : titleNodeList.item(0).getTextContent();

        NodeList authorNodeList = element.getElementsByTagName("author");
        Element authorNode = (Element) authorNodeList.item(0);
        NodeList authorsubNodeList = authorNode.getElementsByTagName("name");
        String name = authorsubNodeList.item(0).getTextContent();
        NodeList profilesubNodeList = authorNode.getElementsByTagName("uri");
        String profileURL = profilesubNodeList.item(0).getTextContent();

        Log.v(TAG, "profileURL = " + profileURL);
        NodeList contentNodeList = element.getElementsByTagName("content");
        Element contentNode = (Element) contentNodeList.item(0);
        String description = contentNode.getTextContent();

        NodeList mediaNodeList = element.getElementsByTagName("media:thumbnail");
        Uri avatarUri = Uri.parse(((Element) mediaNodeList.item(0)).getAttribute("url"));

        Log.v(TAG, "avatarUri = " + avatarUri);
        NodeList publishedNodeList = element.getElementsByTagName("published");
        Element dateNode = (Element) publishedNodeList.item(0);
        String date = dateNode.getTextContent();

        GitHubJourneyWidgetDataEntry entry = new GitHubJourneyWidgetDataEntry(name, avatarUri, eventTitle, description, date);
        Log.v(TAG, "GitHubJourneyWidgetDataEntry = " + entry);
        return entry;
    }

    private ActionType getFeedType(String eventType) {
        switch (eventType) {
            case "ForkEvent":
                return ActionType.FORK;
            case "WatchEvent":
                return ActionType.STAR;
            case "IssueCommentEvent":
                return ActionType.COMMENT;
            case "DeleteEvent":
                return ActionType.DELETE;
            case "CreateEvent":
                return ActionType.CREATE;
            case "IssuesEvent":
                return ActionType.ISSUE;
            case "PushEvent":
                return ActionType.PUSH;
            case "CommitCommentEvent":
                return ActionType.COMMIT_COMMENT;
            case "GollumEvent":
                return ActionType.GOLLUM;
            case "DeploymentEvent":
                return ActionType.DEPLOYMENT;
            case "DeploymentStatusEvent":
                return ActionType.DEPLOYMENT_STATUS;
            case "DownloadEvent":
                return ActionType.DOWNLOAD;
            case "FollowEvent":
                return ActionType.FOLLOW;
            case "ForkApplyEvent":
                return ActionType.FORK_APPLY;
            case "GistEvent":
                return ActionType.GIST;
            case "InstallationEvent":
                return ActionType.INSTALLATION;
            case "InstallationRepositoriesEvent":
                return ActionType.INSTALLATION_REPOSITORIES;
            case "MarketplacePurchaseEvent":
                return ActionType.MARKET_PLACE_PURCHASE;
            case "MemberEvent":
                return ActionType.MEMBER;
            case "MembershipEvent":
                return ActionType.MEMBERSHIP;
            case "MilestoneEvent":
                return ActionType.MILESTONE;
            case "OrganizationEvent":
                return ActionType.ORGANIZATION;
            case "OrgBlockEvent":
                return ActionType.ORG_BLOCK;
            case "PageBuildEvent":
                return ActionType.PAGE_BUILD;
            case "ProjectCardEvent":
                return ActionType.PROJECT_CARD;
            case "ProjectColumnEvent":
                return ActionType.PROJECT_COLUMN;
            case "ProjectEvent":
                return ActionType.PROJECT;
            case "PublicEvent":
                return ActionType.PUBLIC;
            case "PullRequestEvent":
                return ActionType.PULL_REQUEST;
            case "PullRequestReviewEvent":
                return ActionType.PULL_REQUEST_REVIEW;
            case "PullRequestReviewCommentEvent":
                return ActionType.PULL_REQUEST_REVIEW_COMMENT;
            case "ReleaseEvent":
                return ActionType.RELEASE;
            case "RepositoryEvent":
                return ActionType.REPOSITORY;
            case "StatusEvent":
                return ActionType.STATUS;
            case "TeamEvent":
                return ActionType.TEAM;
            case "TeamAddEvent":
                return ActionType.TEAM_ADD;
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
}
