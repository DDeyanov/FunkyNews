package com.deyan.news.funkynews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deyan.news.funkynews.data.FunkyNewsContract.FeedEntry;
import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;

/**
 * Created by Deyan on 13/09/2014.
 */
public class FunkyNewsDbHelper extends SQLiteOpenHelper {

    // This value must be changed if I update the DB schema
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FunkyNews.db";

    public FunkyNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FEED_TABLE =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FeedEntry.COLUMN_FEED_NAME + " TEXT NOT NULL, " +
                FeedEntry.COLUMN_FEED_URL + " TEXT NOT NULL);";

        // The title will be a unique field so a new feed item will be added only if its title is
        // not already in the database. If it is that item will be ignored.
        final String SQL_CREATE_FEED_ITEMS_TABLE =
                "CREATE TABLE " + FeedItemEntry.TABLE_NAME + " (" +
                FeedItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FeedItemEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                FeedItemEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                FeedItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                FeedItemEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                "UNIQUE (" + FeedItemEntry.COLUMN_TITLE + ") ON CONFLICT IGNORE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FEED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FEED_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedItemEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
