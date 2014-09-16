package com.deyan.news.funkynews.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.deyan.news.funkynews.data.FunkyNewsContract.FeedEntry;
import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by Deyan on 15/09/2014.
 */
public class TestDB extends AndroidTestCase {

    private static final String LOG_TAG = TestDB.class.getSimpleName();

    // A test that is checking whether the database can be created. If the database is already
    // created it is deleted before the new one is created.
    public void testCreateDb() throws Throwable {

        mContext.deleteDatabase(FunkyNewsDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new FunkyNewsDbHelper(mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
    }

    // A test that is checking whether new entries can be inserted into the database and then
    // extracted successfully.
    public void testInsertReadDb() {

        FunkyNewsDbHelper dbHelper = new FunkyNewsDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues feedValue = new ContentValues();
        feedValue.put(FeedEntry.COLUMN_FEED_NAME, "Gizmodo");
        feedValue.put(FeedEntry.COLUMN_FEED_URL, "http://gizmodo.com/rss");

        long newFeedRowId = db.insert(FeedEntry.TABLE_NAME, null, feedValue);

        // Verify that the row was inserted
        assertTrue(newFeedRowId != -1);

        ContentValues feedItemValue = new ContentValues();
        feedItemValue.put(FeedItemEntry.COLUMN_TITLE, "This song made with random YouTube music clips is simply amazing");
        feedItemValue.put(FeedItemEntry.COLUMN_LINK, "http://sploid.gizmodo.com/amazing-song-made-with-random-unconnected-youtube-musi-1634773520/+jesusdiaz");
        feedItemValue.put(FeedItemEntry.COLUMN_DESCRIPTION, "<span>I started watching the music video of </span><em>Give It Up</em><span>—the fourth track of Kutiman's first album </span>" +
                "<a href=\"http://thru-you-too.com\" target=\"_blank\">Thru You Too</a>" +
                "<span>—with skepticism. After all, how good can a song be if it's made from samples taken from random, unconnected YouTube music clips? Well, apparently, it can be amazingly good. Just watch and listen.</span>");
        feedItemValue.put(FeedItemEntry.COLUMN_DATE, "2014-09-15");

        long newFeedItemRowId = db.insert(FeedItemEntry.TABLE_NAME, null, feedItemValue);

        // Verify that the row was inserted
        assertTrue(newFeedItemRowId != -1);

        // The data is inserted into the database, now check if it can be pulled.

        Cursor feedCursor = db.query(FeedEntry.TABLE_NAME,
                null, null, null, null, null, null);

        validateCursor(feedCursor, feedValue);

        Cursor feedItemCursor = db.query(FeedItemEntry.TABLE_NAME,
                null, null, null, null, null, null);

        validateCursor(feedItemCursor, feedItemValue);
    }

    // A method that is used to check if the Cursor contains the values from the ContentValues object.
    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        // Assert that there is at least 1 result in the cursor
        assertTrue(valueCursor.moveToFirst());

        // A set of all the keys and values in the provided ContentValues object
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);

            // Assert that there is a column with this name
            assertFalse(idx == -1);

            String expectedValue = entry.getValue().toString();

            assertEquals(expectedValue, valueCursor.getString(idx));
        }

        valueCursor.close();
    }
}
