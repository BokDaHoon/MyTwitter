package com.boostcamp.mytwitter.mytwitter.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by DaHoon on 2017-02-24.
 */

public class TweetService extends IntentService {

    public TweetService() {
        super("TweetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ScheduledTweetTask.executeTask(this, action, intent);
    }
}
