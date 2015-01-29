package com.deyan.news.funkynews.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.deyan.news.funkynews.MainActivity;
import com.deyan.news.funkynews.R;
import com.deyan.news.funkynews.data.FunkyNewsContract;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;
import com.deyan.news.funkynews.parser.RssFeed;
import com.deyan.news.funkynews.parser.RssItem;
import com.deyan.news.funkynews.parser.RssReader;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class handles the transfer of data between a server and an app, using the Android
 * sync adapter framework.
 *
 * Created by Deyan on 28/11/2014.
 */
public class FunkyNewsSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = FunkyNewsSyncAdapter.class.getSimpleName();

    private FunkyNewsDbHelper dbHelper;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.deyan.news.funkynews.sync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "funkynews.deyan.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    // The amount of time in seconds that elapses between periodic syncs. (5 hours)
    public static final int SYNC_INTERVAL = 5 * 60 * 60;
    // The amount of flex time in seconds before SYNC_INTERVAL that is permitted for the sync to take place.
    // Must be less than pollFrequency (SYNC_INTERVAL).
    public static final int SYNC_FLEXTIME = 30 * 60;  // 30 minutes

    public static final int notificationID = 1;


    public FunkyNewsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        dbHelper = new FunkyNewsDbHelper(context);
    }

    // This second form of the constructor is added in Android 3.0 and is required in order to
    // maintain compatibility
    public FunkyNewsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        dbHelper = new FunkyNewsDbHelper(context);
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        Log.i(LOG_TAG, "Performing sync");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Get all feed channels from the database
        Cursor cursor = db.query(
                FunkyNewsContract.FeedEntry.TABLE_NAME,
                new String[] {
                        FunkyNewsContract.FeedEntry._ID,
                        FunkyNewsContract.FeedEntry.COLUMN_FEED_NAME,
                        FunkyNewsContract.FeedEntry.COLUMN_FEED_URL},
                null, null, null, null, null);

        // This list will contain the names of all the feed channels from which there are new feeds.
        // It will then be used to generate a notification.
        List<String> feedChannelsWithNewFeeds = new ArrayList<String>(10);

        while (cursor.moveToNext()) {
            long feedChannelId = cursor.getLong(cursor.getColumnIndex(FunkyNewsContract.FeedEntry._ID));
            String feedChannelName = cursor.getString(cursor.getColumnIndex(FunkyNewsContract.FeedEntry.COLUMN_FEED_NAME));
            String feedChannelURL = cursor.getString(cursor.getColumnIndex(FunkyNewsContract.FeedEntry.COLUMN_FEED_URL));

            long inserted = -1;
            int counter = 0;

            try {

                URL url = new URL(feedChannelURL);
                RssFeed rssFeed = RssReader.read(url);

                String sqlInsertStatement = "INSERT INTO " + FunkyNewsContract.FeedItemEntry.TABLE_NAME +
                        " VALUES (?, ?, ?, ?, ?, ?, ?);";
                SQLiteStatement statement = db.compileStatement(sqlInsertStatement);

                db.beginTransaction();

                // All new items from a feed channel are inserted into the database as a single
                // transaction.
                for (RssItem item : rssFeed.getRssItems()) {
                    statement.clearBindings();
                    statement.bindString(2, item.getTitle());
                    statement.bindString(3, item.getLink());
                    statement.bindString(4, item.getDescription());

                    // Some feed channels do not provide a pubDate element for the items. In that
                    // case the current date and time will be used.
                    if (item.getDate() == null) {
                        statement.bindString(5, FunkyNewsContract.getDateStringForDB(new Date()));
                    } else {
                        statement.bindString(5, FunkyNewsContract.getDateStringForDB(item.getDate()));
                    }

                    statement.bindLong(6, feedChannelId);
                    statement.bindLong(7, 0);
                    inserted = statement.executeInsert();

                    // If a new row is inserted -> add 1 to the counter
                    if (inserted != -1) {
                        counter++;
                    }

                    inserted = -1;
                }

                db.setTransactionSuccessful();
                db.endTransaction();

                if (counter > 0) {
                    feedChannelsWithNewFeeds.add(feedChannelName);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();

        // If the list is not empty, then there are new feed items from some of the feed channels
        if (!feedChannelsWithNewFeeds.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            for (String title : feedChannelsWithNewFeeds) {
                builder.append(title + "  ");
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getContext())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("New feeds from:")
                    .setContentText(builder.toString().trim());

            mBuilder.setAutoCancel(true);

            // Explicit intent
            Intent intent = new Intent(this.getContext(), MainActivity.class);

            // The stack builder object will contain an artificial back stack for the started Activity.
            // This ensures that navigating backward from the Activity leads out of the application
            // to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getContext());
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intent);

            // Get a Pending intent containing the entire back stack
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(notificationID, mBuilder.build());
        }
    }


    public static void initializeSyncAdapter(Context context) {
        Log.i(LOG_TAG, "InitializingSyncAdapter");
        getSyncAccount(context);
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet. If a new account is made, the onAccountCreated()
     * method is called so I can initialize things.
     */
    private static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

            // Add the account and account type, no password and no user data
            // If successful, return the Account object, otherwise report an error.
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            Log.i(LOG_TAG, "Creating account in syncAdapter");
            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }


    private static void onAccountCreated(Account account, Context context) {
        Log.i(LOG_TAG, "onAccountCreated");

        // Since the account is created
        configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        // Without calling setSyncAutomatically, the periodic sync will not be enabled.
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);

        // A sync is done to get things started.
        syncImmediately(context);
    }


    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.i(LOG_TAG, "Configuring periodic sync");

        Account account = getSyncAccount(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // I can use inexact timers for the periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, AUTHORITY).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, AUTHORITY, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     */
    public static void syncImmediately(Context context) {
        Log.i(LOG_TAG, "syncImmediately()");

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), AUTHORITY, bundle);
    }
}
