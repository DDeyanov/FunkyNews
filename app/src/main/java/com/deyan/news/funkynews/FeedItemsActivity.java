package com.deyan.news.funkynews;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class FeedItemsActivity extends ActionBarActivity {

    private static final String LOG_TAG = FeedItemsActivity.class.getSimpleName();

    public static final String FeedUrl = "FeedUrl";
    public static final String FeedId = "FeedId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_items);

        // This provides the Up navigation for the activity.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String feedUrl = getIntent().getStringExtra(FeedUrl);
        Long feedId = getIntent().getLongExtra(FeedId, 0);

        // On a phone this activity will hold a fragment, containing all the feed items from a
        // given feed channel. On a tablet this activity will not be used and the FeedItemsFragment
        // will be placed in the main activity.
        FeedItemsFragment fragment = FeedItemsFragment.newInstance(feedUrl, feedId.toString());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.feed_items_list_container, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed_items, menu);
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
}
