package com.oklab.githubjourney.services;

import android.util.Log;

import com.oklab.githubjourney.asynctasks.AuthenticationAsyncTask;
import com.oklab.githubjourney.data.FeedDataEntry;
import com.oklab.githubjourney.data.FeedType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
 * Created by olgakuklina on 2017-01-15.
 */

public class AtomParser {
    private static final String TAG = AtomParser.class.getSimpleName();
    private final DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
    public List<FeedDataEntry> parse(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = dBFactory.newDocumentBuilder();
        Document xmlDoc = builder.parse(url);
        NodeList nodeList =  xmlDoc.getElementsByTagName("entry");

        List<FeedDataEntry> dataEntriesList = new ArrayList<>(nodeList.getLength());
        for(int i = 0; i < nodeList.getLength(); i++) {
            FeedDataEntry entry = parseItem((Element) nodeList.item(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }

    private FeedDataEntry parseItem(Element element) {
        NodeList idNodeList = element.getElementsByTagName("id");
        if(idNodeList.getLength() == 0) {
            return null;
        }
        String idContent = idNodeList.item(0).getTextContent();
        Log.v(TAG, "idContent - " + idContent);
        int index = idContent.lastIndexOf(":");
        int index2 = idContent.indexOf("/", index);

        String eventType = idContent.substring(index+1, index2);
        FeedType feedType = getFeedType(eventType);
        String eventId = idContent.substring(index2+1);
        long id = Long.parseLong(eventId);

        NodeList titleNodeList = element.getElementsByTagName("title");
        String eventTitle = titleNodeList.getLength() == 0 ? " ": titleNodeList.item(0).getTextContent();

        NodeList authorNodeList = element.getElementsByTagName("author");
        Element authorNode = (Element)authorNodeList.item(0);
        NodeList authorsubNodeList  = authorNode.getElementsByTagName("name");
        String name = authorsubNodeList.item(0).getTextContent();
        NodeList profilesubNodeList  = authorNode.getElementsByTagName("uri");
        String profileURL = profilesubNodeList.item(0).getTextContent();

        NodeList contentNodeList = element.getElementsByTagName("content");
        Element contentNode = (Element)contentNodeList.item(0);
        String description = contentNode.getTextContent();

        NodeList mediaNodeList = element.getElementsByTagName("media:thumbnail");
        String avatarUri = ((Element)mediaNodeList.item(0)).getAttribute("uri");

        NodeList publishedNodeList = element.getElementsByTagName("published");
        Element dateNode = (Element)publishedNodeList.item(0);
        String date = dateNode.getTextContent();
        Calendar entryDate = null;

        FeedDataEntry entry = new FeedDataEntry(id, name, avatarUri, profileURL, eventTitle, description, feedType, entryDate);
        return entry;
    }

    private FeedType getFeedType(String eventType) {
        switch (eventType) {
            case "ForkEvent":
                return FeedType.FORK;
            case "PullRequestEvent":
                return FeedType.PULL_REQUEST;
            case "WatchEvent":
                return FeedType.STAR;
            case  "IssueCommentEvent":
                return FeedType.COMMENT;
            case  "DeleteEvent":
                return FeedType.DELETE;
            case  "CreateEvent":
                return FeedType.CREATE;
            case  "IssuesEvent":
                return FeedType.ISSUE;
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
}
