package com.deyan.news.funkynews.parser;

import java.util.ArrayList;

/**
 * Created by Deyan on 16/09/2014.
 */
public class RssFeed {

    private String title;
    private String link;
    private String description;
    private ArrayList<RssItem> rssItems;

    public RssFeed() {
        rssItems = new ArrayList<RssItem>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<RssItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(ArrayList<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    public void addRssItem(RssItem newItem) {
        rssItems.add(newItem);
    }
}
