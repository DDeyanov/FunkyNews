package com.deyan.news.funkynews.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.deyan.news.funkynews.services.ClearFeedItemsService;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent startServiceIntent = new Intent(context, ClearFeedItemsService.class);
        context.startService(startServiceIntent);
    }
}
