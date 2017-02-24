package com.boostcamp.mytwitter.mytwitter.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.service.ScheduledTweetTask;
import com.boostcamp.mytwitter.mytwitter.service.TweetService;
import com.boostcamp.mytwitter.mytwitter.util.AlarmManagerUtil;

/**
 * Created by DaHoon on 2017-02-24.
 */

public class ScheduleTweetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra("int", -1);
        boolean imageFlag = intent.getBooleanExtra("imageFlag", false);
        boolean replyFlag = intent.getBooleanExtra("replyFlag", false);
        String text = intent.getStringExtra("text");
        String imageUrlPath = intent.getStringExtra("imageUrlPath");

        Log.d("dddd", "Broadcast Receiver");
        Intent nextAlarmIntent = new Intent(context, TweetService.class);
        nextAlarmIntent.setAction(ScheduledTweetTask.ACTION_REGISTER_TWEET);
        nextAlarmIntent.putExtra("id", id);
        nextAlarmIntent.putExtra("text", text);
        nextAlarmIntent.putExtra("imageFlag", imageFlag);
        nextAlarmIntent.putExtra("replyFlag", replyFlag);
        nextAlarmIntent.putExtra("imageUrlPath", imageUrlPath);

        context.startService(nextAlarmIntent);
        unregisterAlarmManager(context, id);
    }

    private void unregisterAlarmManager(Context context, long id) {
        Intent receiverIntent = new Intent(context, ScheduleTweetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int) id,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManagerUtil.from(context).unregisterAlarm(pendingIntent);
    }
}
