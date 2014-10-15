package com.deyan.news.funkynews.data;

import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Deyan on 13/09/2014.
 *
 * This class contains constants for table and column names for the tables that will be
 * contained in the database.
 */
public class FunkyNewsContract {

    // Format used for storing dates in the database as strings. ALso used for converting those date
    // strings back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    // By implementing the BaseColumns interface, this class inherits a primary key called _ID.
    public static final class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "feed";

        public static final String COLUMN_FEED_NAME = "feed_name";
        public static final String COLUMN_FEED_URL = "feed_url";
    }

    public static final class FeedItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "feed_items";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_FOREIGN_KEY = "feed_id";
    }


    public static String getDateStringForDB(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        return sdf.format(date);
    }


    public static Date getDateFromDB(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
