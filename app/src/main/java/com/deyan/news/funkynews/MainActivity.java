package com.deyan.news.funkynews;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;

import com.deyan.news.funkynews.broadcastReceivers.AlarmReceiver;

public class MainActivity extends Activity implements FeedListFragment.OnFeedSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmManager;

    // The pending intent that is triggered when the alarm fires.
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // The database is deleted on each application run. This will be removed when the development
        // process is near the end. For now it allows me to keep only the most recent feeds in the
        // database and not storing huge amount of data.
//        this.deleteDatabase(FunkyNewsDbHelper.DATABASE_NAME);

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + (10 * 1000),
                30 * 1000,
                pendingIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method comes from the OnFeedSelectedInterface and is used when the user select a feed
     * from FeedListFragment.
     *
     * @param url - The url of the feed that is selected
     * @param feedId  - The id of the feed that is selected
     */
    @Override
    public void onItemSelected(String url, long feedId) {

        // TODO - When I have a layout for a tablet this method must be modified. On a tablet it
        // should just add the FeedItemsFragment to this activity without starting a new one.
        Intent intent = new Intent(this, FeedItemsActivity.class);
        intent.putExtra(FeedItemsActivity.FeedUrl, url).putExtra(FeedItemsActivity.FeedId, feedId);

        startActivity(intent);

    }
}
