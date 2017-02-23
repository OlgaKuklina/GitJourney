package com.oklab.githubjourney.services;

import android.util.Log;

import com.oklab.githubjourney.data.ActionType;
import com.oklab.githubjourney.data.GitHubJourneyWidgetDataEntry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

        NodeList contentNodeList = element.getElementsByTagName("content");
        Element contentNode = (Element) contentNodeList.item(0);
        String description = contentNode.getTextContent();

        NodeList mediaNodeList = element.getElementsByTagName("media:thumbnail");
        String avatarUri = ((Element) mediaNodeList.item(0)).getAttribute("uri");

        NodeList publishedNodeList = element.getElementsByTagName("published");
        Element dateNode = (Element) publishedNodeList.item(0);
        String date = dateNode.getTextContent();
        Calendar entryDate = null;

        GitHubJourneyWidgetDataEntry entry = new GitHubJourneyWidgetDataEntry(name, avatarUri, eventTitle, description, date);
        Log.v(TAG, "GitHubJourneyWidgetDataEntry = " + entry);
        return entry;
    }

    private ActionType getFeedType(String eventType) {
        switch (eventType) {
            case "ForkEvent":
                return ActionType.FORK;
            case "PullRequestEvent":
                return ActionType.PULL_REQUEST;
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
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }

}
