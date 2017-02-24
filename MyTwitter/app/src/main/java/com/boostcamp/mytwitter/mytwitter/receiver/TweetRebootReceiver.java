package com.boostcamp.mytwitter.mytwitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boostcamp.mytwitter.mytwitter.service.ScheduledTweetTask;
import com.boostcamp.mytwitter.mytwitter.service.TweetService;

/**
 * Created by DaHoon on 2017-02-24.
 */

public class TweetRebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent rebootIntent = new Intent(context, TweetService.class);
        rebootIntent.setAction(ScheduledTweetTask.ACTION_REBOOT_TWEET);

        context.startService(rebootIntent);
    }
}
