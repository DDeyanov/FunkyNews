package com.deyan.news.funkynews.CustomAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.deyan.news.funkynews.R;
import com.deyan.news.funkynews.data.FunkyNewsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will be used as an adapter for the ListView in FeedItemsFragment. Since the feed
 * items contain HTML tags, the setViewText method is overridden to reflect this.
 *
 * Created by Deyan on 22/09/2014.
 */
public class CustomCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    // The titles of the feed items are keys in the map and the values are flags that show
    // whether the corresponding feed item must be deleted.
    private Map<String, Boolean> itemsToDelete = new HashMap<String, Boolean>();
    private boolean checkAll;

    public CustomCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);

        while(cursor.moveToNext()) {
            itemsToDelete.put(cursor.getString(cursor.getColumnIndex(FunkyNewsContract.FeedItemEntry.COLUMN_TITLE)), false);
        }

        inflater = LayoutInflater.from(context);
    }

    // This method will be called when all of the CheckBoxes in the ListView must be checked/unchecked
    public void setCheckAll(boolean flag) {
        checkAll = flag;

        for(String key : itemsToDelete.keySet()) {
            if (checkAll) {
                // The flags for all items are set to true -> all items must be deleted
                itemsToDelete.put(key, true);
            } else {
                // All flags are set to false -> none of the items will be deleted
                itemsToDelete.put(key, false);
            }
        }

        // This will force all the visible Views to go through the bindView() method again.
        notifyDataSetChanged();
    }

    // Get a list of all the items that are marked for deletion
    public ArrayList<String> getMarkedTitles() {
        ArrayList<String> result = new ArrayList<String>();

        for(String key : itemsToDelete.keySet()) {
            if (itemsToDelete.get(key).booleanValue()) {
                result.add(key);
            }
        }

        return  result;
    }

    // This method is called when a new View is created. Then that new View is passed to the
    // bindView method for setting values to the widgets.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_feed_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView title = (TextView) view.findViewById(R.id.feed_item_title);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.feed_item_checkbox);

        title.setText(cursor.getString(cursor.getColumnIndex(FunkyNewsContract.FeedItemEntry.COLUMN_TITLE)));

        if (checkAll == true) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()) {
                    itemsToDelete.put(title.getText().toString(), true);
                } else {
                    itemsToDelete.put(title.getText().toString(), false);
                }
            }
        });
    }
}
