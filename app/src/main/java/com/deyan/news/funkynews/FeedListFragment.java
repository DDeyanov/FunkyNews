package com.deyan.news.funkynews;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.deyan.news.funkynews.data.FunkyNewsContract.FeedEntry;
import com.deyan.news.funkynews.data.FunkyNewsDbHelper;

/**
 * Created by Deyan on 17/09/2014.
 */
public class FeedListFragment extends Fragment {

    private static final String LOG_TAG = FeedListFragment.class.getSimpleName();

    // In onAttach I'm assigning a MainActivity object to this variable. After that it will be
    // used for communication with FeedItemsFragment (the communication will pass trough MainActivity).
    private OnFeedSelectedListener activityCallback;
    private Cursor cursorForFeeds;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activityCallback = (OnFeedSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement the " +
                    "OnFeedSelectedListener!");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The cursor is initialized here because the fragment will not be destroyed when the
        // activity is destroyed (for example on device rotation). Because of that the onCreate
        // method will be called just one time -> I have to make only one database query.

        // TODO Since the getAllFeeds method requires database access it might be better to get
        // feed channels in some AsyncTask.

        cursorForFeeds = getAllFeeds(getActivity());
    }

    // The method creates the view that is associated with this Fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listView_feeds);

        // Populate the list with the feed channels that have been extracted from the database
        final ListAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_feeds,
                cursorForFeeds,
                new String[] {FeedEntry._ID, FeedEntry.COLUMN_FEED_NAME, FeedEntry.COLUMN_FEED_URL},
                new int[] {R.id.list_item_feed_id, R.id.list_item_feed_name, R.id.list_item_feed_url},
                0
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (cursorForFeeds != null && cursorForFeeds.moveToPosition(position)) {

                    // When an item in the list is clicked, tell MainActivity which of the
                    // feed channels has been selected. Then MainActivity will tell the other
                    // activity/fragment to open the feed items from the selected feed channel.

                    String url = cursorForFeeds.getString(cursorForFeeds.getColumnIndex(FeedEntry.COLUMN_FEED_URL));
                    long feedId = cursorForFeeds.getLong(cursorForFeeds.getColumnIndex(FeedEntry._ID));

                    activityCallback.onItemSelected(url, feedId);
                }
            }
        });

        setRetainInstance(true);

        return rootView;
    }


    public static Cursor getAllFeeds(Activity activity) {

        SQLiteDatabase db = new FunkyNewsDbHelper(activity).getReadableDatabase();

        Cursor cursor = db.query(FeedEntry.TABLE_NAME,
                null, null, null, null, null, null);

        return cursor;
    }


    /*
        An interface that the hosting activity must implement. This mechanism allows the activity
        to be notified of item selections.
     */
    public interface OnFeedSelectedListener {

        public void onItemSelected(String url, long feedId);
    }

}
