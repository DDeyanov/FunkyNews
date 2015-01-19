package com.deyan.news.funkynews.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is responsible for parsing the feed. For each item in the feed an RssItem object is
 * being created and all of those objects are added to a single RssFeed object.
 *
 * Created by Deyan on 16/09/2014.
 */
public class RssHandler extends DefaultHandler {

    private RssFeed feed;
    private RssItem item;
    private StringBuilder stringBuilder;

    public RssFeed getResult() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {

        feed = new RssFeed();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        stringBuilder = new StringBuilder();

        // Each new item/entry is added to the feed object
        if (feed != null && (qName.equals("item") || qName.equals("entry"))) {
            item = new RssItem();
            item.setFeed(feed);
            feed.addRssItem(item);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        stringBuilder.append(ch, start, length);

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // The "if" is executed when the information about the feed channel is being parsed.
        // The "else if" is executed when information about the separate feed items is being parsed.
        if (feed != null && item == null) {

            if (qName.equals("title")) {
                feed.setTitle(stringBuilder.toString());
            } else if (qName.equals("link")) {
                feed.setLink(stringBuilder.toString());
            } else if (qName.equals("description")) {
                feed.setDescription(stringBuilder.toString());
            }
        } else if (item != null) {

            if (qName.equals("title")) {
                item.setTitle(stringBuilder.toString());
            } else if (qName.equals("link")) {
                item.setLink(stringBuilder.toString());
            } else if (qName.equals("description")) {
                item.setDescription(stringBuilder.toString());
            } else if (qName.equals("pubDate")) {
               item.setDate(stringBuilder.toString());
            }
        }
    }
}
