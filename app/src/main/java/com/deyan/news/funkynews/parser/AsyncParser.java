package com.deyan.news.funkynews.parser;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Deyan on 08/09/2014.
 */
public class AsyncParser extends AsyncTask<String, Void, Void> {

    private static final String LOG_TAG = AsyncParser.class.getSimpleName();

    @Override
    protected Void doInBackground(String... params) {

        try {

            URL url = new URL(params[0]);
            RssReader rssReader = new RssReader();
            RssFeed rssFeed = rssReader.read(url);

            for (RssItem item : rssFeed.getRssItems()) {
                Log.i(LOG_TAG, "Title: " + item.getTitle());
                Log.i(LOG_TAG, "Link: " + item.getLink());
                Log.i(LOG_TAG, "Description: " + item.getDescription());
                Log.i(LOG_TAG, "Date: " + item.getDate());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
