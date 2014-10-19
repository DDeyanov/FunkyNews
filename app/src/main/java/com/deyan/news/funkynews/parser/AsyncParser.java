package com.deyan.news.funkynews.parser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;

import com.deyan.news.funkynews.data.FunkyNewsContract;
import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Deyan on 08/09/2014.
 */
public class AsyncParser extends AsyncTask<String, Void, Void> {

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

                statement.bindLong(6, feedId);
                statement.execute();
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

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
