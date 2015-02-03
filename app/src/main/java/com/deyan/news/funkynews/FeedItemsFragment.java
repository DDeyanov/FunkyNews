package com.deyan.news.funkynews;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deyan.news.funkynews.CustomAdapter.CustomCursorAdapter;
import com.deyan.news.funkynews.data.AsyncFeedItemsLoader;
import com.deyan.news.funkynews.data.AsyncFeedItemsUpdater;

import java.util.ArrayList;

public class FeedItemsFragment extends Fragment {

    private static final String LOG_TAG = FeedItemsFragment.class.getSimpleName();

    // the fragment initialization parameters
    private static final String FEED_URL = "FEED_URL";
    private static final String FEED_ID = "FEED_ID";
    private static final String FEED_TITLE = "FEED_TITLE";

    private String feedChannelUrl;
    private String feedChannelId;
    private String feedChannelTitle;

    private TextView channelTitle;
    private ListView mListView;
    private Button deleteMarkedButton;
    private Button markAllButton;

    // Control whether all the rows must be checked or unchecked.
    // true -> checked    false ->unchecked
    private boolean checkedAll = true;

    // I'm assigning a value to this variable when the result from the AsyncFeedItemsLoader task
    // is received. It is then user the re-set the adapter for the list in onCreateView() every
    // time when a device configuration change occurs.
    private CustomCursorAdapter feedItemsAdapter;

    private FeedItemsFragment currentFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url The URL of the feed
     * @param id  The ID of this feed in the database
     * @return A new instance of fragment FeedItemsFragment.
     */
    public static FeedItemsFragment newInstance(String url, String id, String title) {

        FeedItemsFragment fragment = new FeedItemsFragment();
        Bundle args = new Bundle();
        args.putString(FEED_URL, url);
        args.putString(FEED_ID, id);
        args.putString(FEED_TITLE, title);
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
            feedChannelUrl = getArguments().getString(FEED_URL);
            feedChannelId = getArguments().getString(FEED_ID);
            feedChannelTitle = getArguments().getString(FEED_TITLE);
        }

        // Get all feed items from the selected feed channel
        AsyncFeedItemsLoader asyncLoader = new AsyncFeedItemsLoader(getActivity(), this);
        asyncLoader.execute(feedChannelId);

        currentFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        LinearLayout rootLayout =
                (LinearLayout) inflater.inflate(R.layout.fragment_feed_items, container, false);

        channelTitle = (TextView) rootLayout.findViewById(R.id.feed_item_title_for_list);
        channelTitle.setText("News from: " + feedChannelTitle);

        mListView = (ListView) rootLayout.findViewById(R.id.list_of_feed_items);
        deleteMarkedButton = (Button) rootLayout.findViewById(R.id.button_delete_marked);
        markAllButton = (Button) rootLayout.findViewById(R.id.button_mark_all);

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

        deleteMarkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> forDeletion = feedItemsAdapter.getMarkedTitles();
                String[] arr = new String[forDeletion.size()];
                forDeletion.toArray(arr);

                AsyncFeedItemsUpdater task = new AsyncFeedItemsUpdater(getActivity(), currentFragment);
                task.execute(arr);
            }
        });

        markAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedItemsAdapter.setCheckAll(checkedAll);

                checkedAll = !checkedAll;

                // Correct the label of the button to reflect the checked/unchecked state of the
                // checkboxes
                if (!checkedAll) {
                    markAllButton.setText(R.string.button_unmark_all);
                } else {
                    markAllButton.setText(R.string.button_mark_all);
                }
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

        // When the list of feed items is loaded for the first time a new Adapter is instantiated.
        // If an item is deleted the list is updated by just swapping the old cursor with a new one.
        if (feedItemsAdapter == null) {
            feedItemsAdapter = new CustomCursorAdapter(getActivity(), resultCursor, 0);
            mListView.setAdapter(feedItemsAdapter);
        } else {
            feedItemsAdapter.swapCursor(resultCursor);
            feedItemsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * When an update is done (item(s) have been deleted) this method is called from the AsyncTask.
     * It triggers new query which results in a new Cursor. Then that new Cursor is used in the
     * setCursor() method to display the updated ListView.
     */
    public void reloadCursor() {
        AsyncFeedItemsLoader asyncLoader = new AsyncFeedItemsLoader(getActivity(), this);
        asyncLoader.execute(feedChannelId);
    }

}
