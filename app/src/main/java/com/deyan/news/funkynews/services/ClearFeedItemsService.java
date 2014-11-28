package com.deyan.news.funkynews.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deyan.news.funkynews.data.FunkyNewsContract;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A class for handling asynchronous task requests in a service on a separate handler thread.
 * When the onHandleIntent method finishes its work, the service stops itself.
 */
public class ClearFeedItemsService extends IntentService {

    public ClearFeedItemsService() {
        super("ClearFeedItemsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("ClearService", "Inside ClearFeedItemsService");

        // This value must be negative so when it is added to the current date, it sets the range
        // as anything older than 2 hours.
        int rangeInHours = -2;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar range = Calendar.getInstance();
        range.add(Calendar.HOUR_OF_DAY, rangeInHours);

        // Delete the feed items that have been marked for deletion
        SQLiteDatabase db = new FunkyNewsDbHelper(this).getWritableDatabase();

        int deletedRows = db.delete(
            FunkyNewsContract.FeedItemEntry.TABLE_NAME,
            "date < '" + dateFormat.format(range.getTime()) + "' AND for_deletion = '1'",
            null
        );

        Log.i("ClearService", "Deleted rows: " + deletedRows);
    }
}
