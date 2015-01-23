package com.deyan.news.funkynews;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;


public class DetailFeedItemActivity extends Activity {

    public static final String FEED_ITEM_TITLE = "FeedItemTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed_item);

        String title = getIntent().getStringExtra(FEED_ITEM_TITLE);

        TextView titleView = (TextView) findViewById(R.id.detail_feed_item_title);
        TextView descriptionView = (TextView) findViewById(R.id.detail_feed_item_description);
        TextView dateView = (TextView) findViewById(R.id.detail_feed_item_date);

        SQLiteDatabase db = new FunkyNewsDbHelper(this).getReadableDatabase();
        Cursor cursor = db.query(
                FeedItemEntry.TABLE_NAME,
                new String[] {
                        FeedItemEntry.COLUMN_DESCRIPTION,
                        FeedItemEntry.COLUMN_DATE},
                "title = ?",
                new String[] {title},
                null, null, null);

        if (cursor.moveToNext()) {
            titleView.setText(title);

            String description = cursor.getString(cursor.getColumnIndex(FeedItemEntry.COLUMN_DESCRIPTION));

            // If there is a picture in the description - it is removed.
            int indx1 = description.indexOf("<img");
            StringBuilder builder = new StringBuilder(description);

            if (indx1 != -1) {
                int indx2 = description.indexOf("/>", indx1);

                builder.delete(indx1, indx2 + 2);
            }

            descriptionView.setText(Html.fromHtml(builder.toString()));
            descriptionView.setMovementMethod(LinkMovementMethod.getInstance());

            dateView.setText(cursor.getString(cursor.getColumnIndex(FeedItemEntry.COLUMN_DATE)));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_feed_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
