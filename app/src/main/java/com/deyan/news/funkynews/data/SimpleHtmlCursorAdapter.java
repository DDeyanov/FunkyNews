package com.deyan.news.funkynews.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Deyan on 22/09/2014.
 */
public class SimpleHtmlCursorAdapter extends SimpleCursorAdapter {

    public SimpleHtmlCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(TextView view, String text) {

        view.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

    }
}
