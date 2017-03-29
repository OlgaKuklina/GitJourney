package com.oklab.githubjourney.parsers;

import android.util.Log;

import com.oklab.githubjourney.data.ActionType;
import com.oklab.githubjourney.data.ContributionsDataLoader;
import com.oklab.githubjourney.data.FeedDataEntry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by olgakuklina on 2017-01-15.
 */

public class FeedListAtomParser implements AtomParser<FeedDataEntry> {
    private static final String TAG = FeedListAtomParser.class.getSimpleName();
    private final DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    @Override
    public List<FeedDataEntry> parse(String url) throws ParserConfigurationException, IOException, SAXException, ParseException {
        DocumentBuilder builder = dBFactory.newDocumentBuilder();
        Document xmlDoc = builder.parse(url);
        NodeList nodeList = xmlDoc.getElementsByTagName("entry");

        List<FeedDataEntry> dataEntriesList = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            FeedDataEntry entry = parseItem((Element) nodeList.item(i));
            dataEntriesList.add(entry);
        }
        return dataEntriesList;
    }

    private FeedDataEntry parseItem(Element element) throws ParseException {
        NodeList idNodeList = element.getElementsByTagName("id");
        if (idNodeList.getLength() == 0) {
            return null;
        }
        String idContent = idNodeList.item(0).getTextContent();
        Log.v(TAG, "idContent - " + idContent);
        int index = idContent.lastIndexOf(":");
        int index2 = idContent.indexOf("/", index);

        String eventType = idContent.substring(index + 1, index2);
        ActionType actionType = ActionType.getFeedType(eventType);
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
        String avatarUri = ((Element) mediaNodeList.item(0)).getAttribute("url");

        NodeList publishedNodeList = element.getElementsByTagName("published");
        Element dateNode = (Element) publishedNodeList.item(0);
        String feedDate = dateNode.getTextContent();
        Log.v(TAG, "feedDate - " + feedDate);
        Calendar calendarDate = null;
        if (!feedDate.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat(PATTERN);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date entryDate = format.parse(feedDate);
            calendarDate = Calendar.getInstance();
            calendarDate.setTime(entryDate);
            Log.v(TAG, "calendarDate - " + calendarDate);
        }
        FeedDataEntry entry = new FeedDataEntry(id, name, avatarUri, profileURL, eventTitle, description, actionType, calendarDate);
        return entry;
    }
}
