package com.deyan.news.funkynews.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Deyan on 16/09/2014.
 */
public class RssItem {

    private RssFeed feed;
    private String title;
    private String link;
    private String description;
    private Date date;

    public RssItem(){}

    public RssFeed getFeed() {
        return feed;
    }

    public void setFeed(RssFeed feed) {
        this.feed = feed;
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

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
