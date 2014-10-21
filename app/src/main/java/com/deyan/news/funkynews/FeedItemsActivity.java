package com.deyan.news.funkynews;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * On a phone this activity will hold a fragment, containing all the feed items from a given feed
 * channel. On a tablet this activity will not be used and the FeedItemsFragment will be placed in
 * the main activity, next to FeedListFragment.
 */
public class FeedItemsActivity extends Activity {

    private static final String LOG_TAG = FeedItemsActivity.class.getSimpleName();

    private static final String FRAGMENT_TAG = "feed_items_fragment";

    public static final String FeedUrl = "FeedUrl";
    public static final String FeedId = "FeedId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_items);

        // This provides the Up navigation for the activity.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String feedUrl = getIntent().getStringExtra(FeedUrl);
        Long feedId = getIntent().getLongExtra(FeedId, 0);

        FragmentManager manager = getFragmentManager();
        FeedItemsFragment fragment = (FeedItemsFragment) manager.findFragmentByTag(FRAGMENT_TAG);

        // Only one fragment will be created -> the first time when this activity is launched.
        // That fragment will be retained and when a configuration change occurs the code in that
        // if() won't be executed.
        if (fragment == null) {

            fragment = FeedItemsFragment.newInstance(feedUrl, feedId.toString());

            manager.beginTransaction()
                .add(R.id.feed_items_list_container, fragment, FRAGMENT_TAG)
                .commit();

        }
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
