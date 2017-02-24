package com.boostcamp.mytwitter.mytwitter.timeline.model;

import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class TimelineModel {

    private ArrayList<twitter4j.Status> dataList = new ArrayList<>();
    private ModelDataChange mModelDataChange;

    public interface ModelDataChange {
        void update(List<Status> list);
    }

    /**
     * Search Github Open API
     */
    public void callTimelineList(int currentPage) {
        new BindTimelineTask().execute(currentPage);
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

    class BindTimelineTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            Twitter mTwit = TwitterInfo.TwitInstance;
            int currentPage = params[0];

            try {
                Paging paging = new Paging(currentPage, 20);
                final List<twitter4j.Status> statuses = mTwit.getHomeTimeline(paging);
                dataList.addAll(statuses);

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Timeline Activity", "실패");

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dataList != null) {
                mModelDataChange.update(dataList);
            }
        }


    }

}
