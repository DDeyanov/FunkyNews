package com.deyan.news.funkynews;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.deyan.news.funkynews.data.AsyncFeedItemsLoader;
import com.deyan.news.funkynews.data.FunkyNewsContract;
import com.deyan.news.funkynews.parser.AsyncParser;

public class FeedItemsFragment extends Fragment {

    private static final String LOG_TAG = FeedItemsFragment.class.getSimpleName();

    // the fragment initialization parameters
    private static final String FEED_URL = "FEED_URL";
    private static final String FEED_ID = "FEED_ID";

    private String feedUrl;
    private String feedId;

    private ListView mListView;

    // I'm assigning a value to this variable when the result from the AsyncFeedItemsLoader task
    // is received. It is then user the re-set the adapter for the list in onCreateView() every
    // time when a device configuration change occurs.
    private ListAdapter feedItemsAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url The URL of the feed
     * @param id  The ID of this feed in the database
     * @return A new instance of fragment FeedItemsFragment.
     */
    public static FeedItemsFragment newInstance(String url, String id) {

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

        AsyncFeedItemsLoader asyncLoader = new AsyncFeedItemsLoader(getActivity(), this);
        asyncLoader.execute(feedId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        LinearLayout rootLayout =
                (LinearLayout) inflater.inflate(R.layout.fragment_feed_items, container, false);

        mListView = (ListView) rootLayout.findViewById(R.id.list_of_feed_items);

        // When the user click on a title the DetailFeedItemActivity is opened with the
        // description for this feed item.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView title = (TextView) view.findViewById(R.id.feed_item_title);

                Intent intent = new Intent(getActivity(), DetailFeedItemActivity.class);
                intent.putExtra(DetailFeedItemActivity.FEED_ITEM_TITLE, title.getText());

                startActivity(intent);
            }
        });

        // On device rotation (or any other configuration change) the fragment will be retained, but
        // that method will be called again. Because of that, the adapter that is being set the first
        // time when the fragment is created must be set again here. If it is not set, the results
        // from the database will not appear on screen.
        if (feedItemsAdapter != null) {
            mListView.setAdapter(feedItemsAdapter);
        }

        // Indicate that the fragment instance must be retained across activity recreation.
        // onDestroy() and onCreate() will not be called, but onDetach() and onAttach() will
        // be called.
        setRetainInstance(true);

        return rootLayout;
    }


    public void setCursor(Cursor resultCursor) {

        ListAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_feed_items,
                resultCursor,
                new String [] {FunkyNewsContract.FeedItemEntry.COLUMN_TITLE},
                new int[] {R.id.feed_item_title},
                0
        );

        feedItemsAdapter = adapter;

        if (mListView == null) {
            Log.i(LOG_TAG, "ListView (The root view) is null");
        } else {
            mListView.setAdapter(adapter);
        }
    }
}
