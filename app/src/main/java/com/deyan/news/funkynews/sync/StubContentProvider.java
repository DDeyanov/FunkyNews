package com.deyan.news.funkynews.sync;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * The sync adapter framework requires a content provider, so this is an implementation of
 * ContentProvider that stubs all methods
 *
 * Created by Deyan on 28/11/2014.
 */
public class StubContentProvider extends ContentProvider {

    // Always return true. This indicates that the provider loaded correctly
    @Override
    public boolean onCreate() {
        return true;
    }

    // The query always return no results
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        return null;
    }

    // Return an empty String for the MIME type
    @Override
    public String getType(Uri uri) {
        return new String();
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    // Delete always returns "no rows affected" (0)
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
