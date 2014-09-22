package com.deyan.news.funkynews.parser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;

import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Deyan on 08/09/2014.
 */
public class AsyncParser extends AsyncTask<String, Void, Void> {

    private static final String LOG_TAG = AsyncParser.class.getSimpleName();
    private static final String LOG_TAG2 = "ModifiedTag";

    private Context mContext;

    public AsyncParser(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {

            URL url = new URL(params[0]);
            RssFeed rssFeed = RssReader.read(url);

            FunkyNewsDbHelper dbHelper = new FunkyNewsDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            long feedId = Long.parseLong(params[1]);

            String sqlInsertStatement = "INSERT INTO " + FeedItemEntry.TABLE_NAME +
                    " VALUES (?, ?, ?, ?, ?, ?);";
            SQLiteStatement statement = db.compileStatement(sqlInsertStatement);

            db.beginTransaction();

            for (RssItem item : rssFeed.getRssItems()) {
//                Log.i(LOG_TAG, "Title: " + item.getTitle());
//                Log.i(LOG_TAG, "Link: " + item.getLink());
//                Log.i(LOG_TAG, "Description: " + item.getDescription());
//                Log.i(LOG_TAG, "Date: " + item.getDate());
                statement.clearBindings();
                statement.bindString(2, item.getTitle());
                statement.bindString(3, item.getLink());
                statement.bindString(4, item.getDescription());
                statement.bindString(5, item.getDate().toString());
                statement.bindLong(6, feedId);
                statement.execute();
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

//            Cursor results = db.query(FeedItemEntry.TABLE_NAME,
//                    null, null, null, null, null, null);
//
//            while (results.moveToNext()) {
//
//                int titleIndx = results.getColumnIndex(FeedItemEntry.COLUMN_TITLE);
//                String title = results.getString(titleIndx);
//
//                int linkIndx = results.getColumnIndex(FeedItemEntry.COLUMN_LINK);
//                String link = results.getString(linkIndx);
//
//                int descriptionIndx = results.getColumnIndex(FeedItemEntry.COLUMN_DESCRIPTION);
//                String description = results.getString(descriptionIndx);
//
//                int dateIndx = results.getColumnIndex(FeedItemEntry.COLUMN_DATE);
//                String date = results.getString(dateIndx);
//
//                int feedIdIndx = results.getColumnIndex(FeedItemEntry.COLUMN_FOREIGN_KEY);
//                long feedID = results.getLong(feedIdIndx);
//
//                Log.i(LOG_TAG2, "Title: " + title);
//                Log.i(LOG_TAG2, "Link: " + link);
//                Log.i(LOG_TAG2, "Description: " + description);
//                Log.i(LOG_TAG2, "Date: " + date);
//                Log.i(LOG_TAG2, "Feed_id: " + feedID);
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
