package com.boostcamp.mytwitter.mytwitter.base;

import android.app.Application;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import okhttp3.OkHttpClient;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class MyTwitterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final TwitterSession activeSession = TwitterCore.getInstance()
                .getSessionManager().getActiveSession();

        // example of custom OkHttpClient with logging HttpLoggingInterceptor
        final OkHttpClient customClient = new OkHttpClient.Builder().build();

        // pass custom OkHttpClient into TwitterApiClient and add to TwitterCore
        final TwitterApiClient customApiClient;
        if (activeSession != null) {
            customApiClient = new TwitterApiClient(activeSession, customClient);
            TwitterCore.getInstance().addApiClient(activeSession, customApiClient);
        } else {
            customApiClient = new TwitterApiClient(customClient);
            TwitterCore.getInstance().addGuestApiClient(customApiClient);
        }
    }
}
