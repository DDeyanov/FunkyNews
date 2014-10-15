package com.deyan.news.funkynews.test;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.deyan.news.funkynews.FeedListFragment;
import com.deyan.news.funkynews.MainActivity;
import com.deyan.news.funkynews.R;

/**
 * Created by Deyan on 24/09/2014.
 */
public class TestFeedActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private ListView feedsList;

    public TestFeedActivity() {
        super(MainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

        feedsList = (ListView) activity.findViewById(R.id.listView_feeds);
    }


    public void testPreconditions() {
        assertNotNull("Activity is null", activity);
        assertNotNull("ListView is null", feedsList);
    }


    public void testPopulateFeedList() {
        Cursor cursor = FeedListFragment.getAllFeeds(activity);

        assertEquals(cursor.getCount(), feedsList.getCount());
    }

    public void testDeviceRotation() {

        int firstNumberOfItems = feedsList.getCount();

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        int secondNumberOfItems = feedsList.getCount();

        assertEquals(firstNumberOfItems, secondNumberOfItems);
    }

}
