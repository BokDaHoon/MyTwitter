package com.boostcamp.mytwitter.mytwitter.write.model;

import android.os.AsyncTask;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * Created by DaHoon on 2017-02-13.
 */

public class WriteModel {

    public void callWriteContent(StatusUpdate content) {
        new CallWriteContentTask().execute(content);
    }

    class CallWriteContentTask extends AsyncTask<StatusUpdate, Void, Void> {

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
