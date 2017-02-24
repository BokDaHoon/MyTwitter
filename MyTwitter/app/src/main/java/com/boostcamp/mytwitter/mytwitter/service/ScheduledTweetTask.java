package com.boostcamp.mytwitter.mytwitter.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.base.db.TweetRealmObject;
import com.boostcamp.mytwitter.mytwitter.receiver.ScheduleTweetReceiver;
import com.boostcamp.mytwitter.mytwitter.util.AlarmManagerUtil;
import com.boostcamp.mytwitter.mytwitter.util.Define;

import java.io.File;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * Created by DaHoon on 2017-02-24.
 */

public class ScheduledTweetTask {

    public static final String ACTION_REBOOT_TWEET = "reboot_tweet";
    public static final String ACTION_REGISTER_TWEET = "register_tweet";

    public static void executeTask(Context context, String action, Intent intent) {
        if(action.equals(ACTION_REGISTER_TWEET)) {
            executeRegisterTweet(context, intent);
        } else if (action.equals(ACTION_REBOOT_TWEET)) {
            executeRebootTweet(context, intent);
        }
    }

    private static void executeRebootTweet(Context context, Intent intent) {
        Realm mRealm = Realm.getDefaultInstance();
        RealmQuery<TweetRealmObject> query = mRealm.where(TweetRealmObject.class);

        RealmResults<TweetRealmObject> result = query.findAll();
        for(TweetRealmObject tweet : result){
            String text = tweet.getText();
            String imageUrlPathTweet = tweet.getImagePath();
            boolean replyFlag = tweet.isReplyFlag();
            boolean imageFlag = tweet.isImageFlag();
            long id = tweet.getId();
            Date settingDate = tweet.getScheduleDate();

            Intent receiverIntent = new Intent(context, ScheduleTweetReceiver.class);
            receiverIntent.putExtra("id", id);
            receiverIntent.putExtra("text", text);
            receiverIntent.putExtra("imageFlag", imageFlag);
            receiverIntent.putExtra("replyFlag", replyFlag);
            receiverIntent.putExtra("imageUrlPath", imageUrlPathTweet);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    (int) id,
                    receiverIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManagerUtil.from(context).setAlarm(settingDate, pendingIntent);
        }
    }

    private static void executeRegisterTweet(Context context, Intent intent) {

        Log.d("dddd", "Service");
        long id = intent.getLongExtra("int", -1);
        boolean imageFlag = intent.getBooleanExtra("imageFlag", false);
        boolean replyFlag = intent.getBooleanExtra("replyFlag", false);
        String text = intent.getStringExtra("text");
        String imageUrlPath = intent.getStringExtra("imageUrlPath");

        StatusUpdate content = new StatusUpdate(text);

        if (imageFlag) {
            File file = new File(imageUrlPath);

            content.setMedia(file);
        }

        // 댓글인 경우
        if (replyFlag) {
            content.setInReplyToStatusId(intent.getLongExtra(Define.TWEET_ID_KEY, -1));
        }

        new CallWriteContentTask().execute(content);
    }

    static class CallWriteContentTask extends AsyncTask<StatusUpdate, Void, Void> {

        @Override
        protected Void doInBackground(StatusUpdate... params) {

            try {
                twitter4j.Status result = TwitterInfo.TwitInstance.updateStatus(params[0]);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
