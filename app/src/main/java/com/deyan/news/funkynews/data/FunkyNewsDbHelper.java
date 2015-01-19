package com.deyan.news.funkynews.data;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Deyan on 13/09/2014.
 */
public class FunkyNewsDbHelper extends SQLiteAssetHelper {

    // This value must be changed if I update the DB schema
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FunkyNews.db";

    public FunkyNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
