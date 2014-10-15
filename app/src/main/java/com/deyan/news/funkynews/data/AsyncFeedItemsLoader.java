package com.deyan.news.funkynews.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.deyan.news.funkynews.FeedItemsFragment;
import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;

/**
 * Created by Deyan on 22/09/2014.
 *
 * The class will be used to asynchronously query the FeedItems table in the database for feed items
 * with a given feed_id;
 */
public class AsyncFeedItemsLoader extends AsyncTask<String, Void, Cursor> {

    private Context mContext;
    private FeedItemsFragment mFragment;

    public AsyncFeedItemsLoader(Context context, FeedItemsFragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    /**
     *
     * @param strings  strings array contains only 1 value which is the id of the feed that the user
     *                 have selected.
     * @return
     */
    @Override
    protected Cursor doInBackground(String... strings) {

        SQLiteDatabase db = new FunkyNewsDbHelper(mContext).getReadableDatabase();

        Cursor cursor = db.query(FeedItemEntry.TABLE_NAME,
                new String[] {
                        FeedItemEntry._ID,
                        FeedItemEntry.COLUMN_TITLE,
                        FeedItemEntry.COLUMN_DESCRIPTION,
                        FeedItemEntry.COLUMN_DATE,
                        FeedItemEntry.COLUMN_LINK},
                " feed_id = ?",
                new String[] {strings[0]},
                null, null, FeedItemEntry.COLUMN_DATE + " ASC");

        return cursor;
    }


    @Override
    protected void onPostExecute(Cursor cursor) {

        mFragment.setCursor(cursor);

    }
}
