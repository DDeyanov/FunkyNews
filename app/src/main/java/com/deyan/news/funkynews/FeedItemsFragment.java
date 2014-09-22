package com.deyan.news.funkynews;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.deyan.news.funkynews.data.AsyncFeedItemsLoader;
import com.deyan.news.funkynews.data.FunkyNewsContract.FeedItemEntry;
import com.deyan.news.funkynews.data.SimpleHtmlCursorAdapter;
import com.deyan.news.funkynews.parser.AsyncParser;


public class FeedItemsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FEED_URL = "FEED_URL";
    private static final String FEED_ID = "FEED_ID";

    private String feedUrl;
    private String feedId;

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

        //TODO - A SyncAdapter must be used instead AsyncParser
        AsyncParser parser = new AsyncParser(getActivity());
        parser.execute(feedUrl, feedId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ListView rootView = (ListView) inflater.inflate(R.layout.fragment_feed_items, container, false);

        AsyncFeedItemsLoader asyncLoader = new AsyncFeedItemsLoader(getActivity(), this, rootView);
        asyncLoader.execute(feedId);

        return rootView;
    }

    public void setAdapterToList(ListView listView, Cursor cursor) {

        ListAdapter adapter = new SimpleHtmlCursorAdapter(
                getActivity(),
                R.layout.list_feed_items,
                cursor,
                new String [] {FeedItemEntry.COLUMN_TITLE, FeedItemEntry.COLUMN_DESCRIPTION,
                               FeedItemEntry.COLUMN_DATE, FeedItemEntry.COLUMN_LINK},
                new int[] {R.id.feed_item_title, R.id.feed_item_description,
                        R.id.feed_item_date, R.id.feed_item_link},
                0
        );

        listView.setAdapter(adapter);
    }
}
