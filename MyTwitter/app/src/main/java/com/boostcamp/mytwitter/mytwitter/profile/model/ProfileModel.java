package com.boostcamp.mytwitter.mytwitter.profile.model;

import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-16.
 */

public class ProfileModel {

    private ModelDataChange mModelDataChange;

    public interface ModelDataChange {
        void update(List<Status> list);
    }

    public void setOnChangeListener(ModelDataChange dataChange) {
        mModelDataChange = dataChange;
    }

    public User getUserInfo(long id) {

        User result = null;
        Log.d("dddd", id + "");
        try {
            result = new GetUserInfoTask().execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void callLoadUserTweetList(long id) {
        new BindUserTweetTask().execute(id);
    }

    class GetUserInfoTask extends AsyncTask<Long, Void, User> {

        @Override
        protected User doInBackground(Long... params) {
            Twitter mTwit = TwitterInfo.TwitInstance;
            List<User> user = null;
            long[] queryParam = null;
            try {
                long id = params[0];

                if (id == -1){ // MyProfile
                    queryParam = new long[]{mTwit.getId()};
                } else {       // OtherProfile
                    queryParam = new long[]{id};
                }

                user = mTwit.lookupUsers(queryParam);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return user.get(0);
        }
    }

    class BindUserTweetTask extends AsyncTask<Long, Void, List<Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(Long... params) {

            Twitter mTwit = TwitterInfo.TwitInstance;

            try {
                final List<twitter4j.Status> statuses = mTwit.getUserTimeline(params[0]);

                return statuses;

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Timeline Activity", "실패");
                return null;

            }

        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            if (statuses != null) {
                mModelDataChange.update(statuses);
            }
        }

    }

}
