package com.deyan.news.funkynews;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.deyan.news.funkynews.data.AsyncFeedItemsLoader;
import com.deyan.news.funkynews.data.FunkyNewsContract;
import com.deyan.news.funkynews.data.SimpleHtmlCursorAdapter;
import com.deyan.news.funkynews.parser.AsyncParser;


public class FeedItemsFragment extends Fragment {

    private static final String LOG_TAG = FeedItemsFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FEED_URL = "FEED_URL";
    private static final String FEED_ID = "FEED_ID";

    private String feedUrl;
    private String feedId;

    private Cursor cursorForFeedItems;

    private ListView rootListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url The URL of the feed
     * @param id  The ID of this feed in the database
     * @return A new instance of fragment FeedItemsFragment.
     */
    public static FeedItemsFragment newInstance(String url, String id) {

        Log.i(LOG_TAG, "Into newInstance()");

        FeedItemsFragment fragment = new FeedItemsFragment();
        Bundle args = new Bundle();
        args.putString(FEED_URL, url);
        args.putString(FEED_ID, id);
        fragment.setArguments(args);

        return fragment;
    }


    public FeedItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            feedUrl = getArguments().getString(FEED_URL);
            feedId = getArguments().getString(FEED_ID);
        }

        // Get all feed items from the selected feed channel and if some of the items are not in
        // the database -> insert them
        //TODO - A SyncAdapter must be used instead AsyncParser
        AsyncParser parser = new AsyncParser(getActivity());
        parser.execute(feedUrl, feedId);

        Log.i(LOG_TAG, "inside onCreate");

        AsyncFeedItemsLoader asyncLoader = new AsyncFeedItemsLoader(getActivity(), this);
        asyncLoader.execute(feedId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootListView = (ListView) inflater.inflate(R.layout.fragment_feed_items, container, false);

        // Indicate that the fragment instance must be retained across activity recreation.
        // onDestroy() and onCreate() will not be called, but onDetach() and onAttach() will
        // be called.
        setRetainInstance(true);

        return rootListView;
    }


    public void setCursor(Cursor cursor) {

        Log.i(LOG_TAG, "Setting cursor to local cursor");

        cursorForFeedItems = cursor;

        ListAdapter adapter = new SimpleHtmlCursorAdapter(
                getActivity(),
                R.layout.list_feed_items,
                cursorForFeedItems,
                new String [] {FunkyNewsContract.FeedItemEntry.COLUMN_TITLE, FunkyNewsContract.FeedItemEntry.COLUMN_DESCRIPTION,
                        FunkyNewsContract.FeedItemEntry.COLUMN_DATE, FunkyNewsContract.FeedItemEntry.COLUMN_LINK},
                new int[] {R.id.feed_item_title, R.id.feed_item_description,
                        R.id.feed_item_date, R.id.feed_item_link},
                0
        );

        if (rootListView == null) {
            Log.i(LOG_TAG, "ListView is null");
        } else {
            Log.i(LOG_TAG, "ListView is not null");
            rootListView.setAdapter(adapter);
        }
    }
}
