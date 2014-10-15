package com.deyan.news.funkynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.deyan.news.funkynews.data.FunkyNewsDbHelper;

public class MainActivity extends ActionBarActivity implements FeedListFragment.OnFeedSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.deleteDatabase(FunkyNewsDbHelper.DATABASE_NAME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method comes from the OnFeedSelectedInterface and is used when the user select a feed
     * from FeedListFragment.
     *
     * @param url - The url of the feed that is selected
     * @param feedId  - The id of the feed that is selected
     */
    @Override
    public void onItemSelected(String url, long feedId) {

        // TODO - When I have a layout for a tablet this method must be modified. On a tablet it
        // should just add the FeedItemsFragment to this activity without starting a new one.
        Intent intent = new Intent(this, FeedItemsActivity.class);
        intent.putExtra(FeedItemsActivity.FeedUrl, url).putExtra(FeedItemsActivity.FeedId, feedId);

        startActivity(intent);

    }
}
