package com.boostcamp.mytwitter.mytwitter.timeline.model;

import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class TimelineModel {

    private ModelDataChange mModelDataChange;

    public interface ModelDataChange {
        void update(List<Status> list);
    }

    /**
     * Search Github Open API
     */
    public void callTimelineList() {
        new BindTimelineTask().execute();
    }

    public User getUserInfo() {

        User result = null;

        try {
            result = new GetUserInfoTask().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Set Listener
     * @param dataChange
     */
    public void setOnChangeListener(ModelDataChange dataChange) {
        mModelDataChange = dataChange;
    }

    class GetUserInfoTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            Twitter mTwit = TwitterInfo.TwitInstance;
            List<User> user = null;
            try {
                user = mTwit.lookupUsers(new long[]{mTwit.getId()});
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return user.get(0);
        }
    }

    class BindTimelineTask extends AsyncTask<Void, Void, List<Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(Void... params) {

            Twitter mTwit = TwitterInfo.TwitInstance;

            try {
                final List<twitter4j.Status> statuses = mTwit.getHomeTimeline();

                for (twitter4j.Status status : statuses) {
                    Log.d("Timeline Model", status.toString());
                }

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
