package com.boostcamp.mytwitter.mytwitter.detail.model;

import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.detail.presenter.DetailPresenter;
import com.boostcamp.mytwitter.mytwitter.detail.presenter.DetailPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by DaHoon on 2017-02-15.
 */

public class DetailModel {

    private DetailModel.ModelDataChange mModelDataChange;
    DetailPresenterImpl presenter;

    public void setPresenter(DetailPresenterImpl detailPresenter) {
        this.presenter = detailPresenter;
    }

    public interface ModelDataChange {
        void update(List<Status> list);
    }


    public void setOnChangeListener(DetailModel.ModelDataChange dataChange) {
        mModelDataChange = dataChange;
    }

    public void callLoadReplyList(Status status) {
        new BindUserTweetTask().execute(status);
    }

    class BindUserTweetTask extends AsyncTask<Status, Void, List<Status>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            presenter.showProgressBar();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(twitter4j.Status... params) {

            twitter4j.Status status = params[0];
            Twitter mTwit = TwitterInfo.TwitInstance;

            ArrayList<twitter4j.Status> replies = new ArrayList<>();


            try {
                long id = status.getId();
                String screenname = status.getUser().getScreenName();

                Query query = new Query("@" + screenname + " since_id:" + id);
                query.resultType(Query.RECENT);

                QueryResult result = mTwit.search(query);
                final List<twitter4j.Status> statuses = mTwit.getUserTimeline(screenname);
                for (twitter4j.Status tweet : statuses) {
                    if (tweet.getInReplyToStatusId() == id) {
                        replies.add(tweet);
                    }
                }
/*
                do {
                    Log.d("Query", query.toString());
                    List<twitter4j.Status> tweets = result.getTweets();

                    for (twitter4j.Status tweet : tweets) {
                        if (tweet.getInReplyToStatusId() == id) {
                            replies.add(tweet);
                        }
                    }

                    query = result.nextQuery();

                    if (query != null) {
                        result = mTwit.search(query);
                    }

                } while (query != null);*/

            } catch (Exception e) {
                e.printStackTrace();
            }

            return replies;

        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            presenter.disappearProgressBar();
            if (statuses != null) {
                mModelDataChange.update(statuses);
            }
        }

    }
}
