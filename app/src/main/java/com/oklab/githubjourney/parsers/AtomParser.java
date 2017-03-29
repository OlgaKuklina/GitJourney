package com.oklab.githubjourney.parsers;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by olgakuklina on 2017-02-22.
 */
public interface AtomParser<T> {
    List<T> parse(String url) throws ParserConfigurationException, IOException, SAXException, ParseException;
}
