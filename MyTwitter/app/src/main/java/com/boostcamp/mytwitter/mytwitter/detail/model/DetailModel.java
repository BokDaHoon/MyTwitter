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
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by DaHoon on 2017-02-15.
 */

public class DetailModel {

    private DetailModel.ModelDataChange mModelDataChange;
    DetailPresenterImpl presenter;
    BindUserTweetTask task;

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
        task = new BindUserTweetTask();
        task.execute(status);
    }

    public void finishedTask() {
        task.cancel(true);
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
            ArrayList<twitter4j.Status> all = null;

            try {
                long id = status.getId();
                String screenname = status.getUser().getScreenName();

                Query query = new Query("@" + screenname + " since_id:" + id);

                try {
                    query.setCount(100);
                } catch (Throwable e) {
                    query.setCount(30);
                }

                QueryResult result = mTwit.search(query);
                System.out.println("result: " + result.getTweets().size());

                all = new ArrayList<>();

                do {
                    List<twitter4j.Status> tweets = result.getTweets();

                    for (twitter4j.Status tweet : tweets)
                        if (tweet.getInReplyToStatusId() == id)
                            all.add(tweet);

                    if (all.size() > 0) {
                        for (int i = all.size() - 1; i >= 0; i--) {
                            replies.add(all.get(i));
                        }
                        all.clear();
                    }

                    query = result.nextQuery();

                    if (query != null)
                        result = mTwit.search(query);

                } while (query != null && replies.size() < 5 && presenter.checkFinished());

            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            return replies;

        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            if (presenter.checkFinished() == true) {
                return;
            }
            presenter.disappearProgressBar();
            if (statuses != null) {
                mModelDataChange.update(statuses);
            }
        }

    }
}
