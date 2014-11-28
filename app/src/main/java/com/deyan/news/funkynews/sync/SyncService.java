package com.deyan.news.funkynews.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Define a Service that returns an IBinder for the sync adapter class, allowing the sync adapter
 * framework to call onPerformSync().
 *
 * Created by Deyan on 28/11/2014.
 */
public class SyncService extends Service {

    private static FunkyNewsSyncAdapter adapter = null;

    // Object to use as a thread-safe lock
    private static final Object syncAdapterLock = new Object();

    // Instantiate the SyncAdapter object
    @Override
    public void onCreate() {

        // Create the sync adapter as a singleton. Disallow parallel syncs.
        synchronized (syncAdapterLock) {
            if (adapter == null) {
                adapter = new FunkyNewsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    // Return an object that allows the system to invoke the sync adapter
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter constructors call super()
         */
        return adapter.getSyncAdapterBinder();
    }
}
