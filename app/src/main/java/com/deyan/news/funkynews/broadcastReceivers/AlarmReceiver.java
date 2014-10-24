package com.deyan.news.funkynews.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.deyan.news.funkynews.services.ClearFeedItemsService;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AlarmReceiver", "Received an alarm");

        Intent startServiceIntent = new Intent(context, ClearFeedItemsService.class);
        context.startService(startServiceIntent);

    }
}
