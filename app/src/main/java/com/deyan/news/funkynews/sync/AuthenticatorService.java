package com.deyan.news.funkynews.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A bound service that instantiates the Authenticator when started.
 *
 * Created by Deyan on 28/11/2014.
 */
public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    // When the system binds to this service to make the RPC call return the authenticator's IBinder
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
