package com.deyan.news.funkynews.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

        // The images in the feeds (if there are images) are not being displayed properly so they
        // are removed in the next couple of lines.
        int indx1 = text.indexOf("<img");
        StringBuilder builder = new StringBuilder(text);;

        if (indx1 != -1) {

            int indx2 = text.indexOf("/>", indx1);

            builder.delete(indx1, indx2 + 2);
        }

        view.setText(Html.fromHtml(builder.toString()), TextView.BufferType.NORMAL);

        // This makes the links in the TextView clickable
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
