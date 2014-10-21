package com.deyan.news.funkynews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.deyan.news.funkynews.FeedItemsFragment;

/**
 * Created by Deyan on 21/10/2014.
 */
public class AsyncFeedItemsUpdater extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private FeedItemsFragment mFragment;

    public AsyncFeedItemsUpdater(Context context, FeedItemsFragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    @Override
    protected Void doInBackground(String... strings) {

        SQLiteDatabase db = new FunkyNewsDbHelper(mContext).getWritableDatabase();

        ContentValues valuesToUpdate = new ContentValues();
        valuesToUpdate.put(FunkyNewsContract.FeedItemEntry.COLUMN_FOR_DELETION_FLAG, 1);

        db.beginTransaction();

        for (int i = 0; i < strings.length; i++) {
            db.update(
                    FunkyNewsContract.FeedItemEntry.TABLE_NAME,
                    valuesToUpdate,
                    "title = ?",
                    new String[] {strings[i]}
            );
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mFragment.reloadCursor();
    }
}
