package com.deyan.news.funkynews.data;

import android.provider.BaseColumns;

/**
 * Created by Deyan on 13/09/2014.
 *
 * This class contains constants for table and column names for the tables that will be
 * contained in the database.
 */
public class FunkyNewsContract {

    // By implementing the BaseColumns interface, this class inherits a primary key called _ID.
    public static final class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "feed";

        public static final String COLUMN_FEED_NAME = "feed_name";
        public static final String COLUMN_FEED_URL = "feed_url";
    }

    public static final class FeedItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "item";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
    }
}
