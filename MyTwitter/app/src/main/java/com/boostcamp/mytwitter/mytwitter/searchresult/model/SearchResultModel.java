package com.boostcamp.mytwitter.mytwitter.searchresult.model;

import android.os.AsyncTask;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-20.
 */

public class SearchResultModel {

    private ModelDataChange mModelDataChange;

    public interface ModelDataChange {
        void update(ResponseList<User> list);
    }

    public void setOnChangeListener(ModelDataChange dataChange) {
        mModelDataChange = dataChange;
    }

    public void callLoadSearchResult(String name) throws ExecutionException, InterruptedException {
        new SearchTask().execute(name);
    }

    class SearchTask extends AsyncTask<String, Void, ResponseList<User>> {

        @Override
        protected ResponseList<User> doInBackground(String... params) {
            try {
                Twitter twitter = TwitterInfo.TwitInstance;
                String name = params[0];
                int page = 1;
                ResponseList<User> users;

                do {
                    users = twitter.searchUsers(name, page);

                    for (User user : users) {
                        if (user.getStatus() != null) {
                            System.out.println("@" + user.getScreenName() + " - " + user.getName());
                        } else {
                            // the user is protected
                            System.out.println("@" + user.getScreenName());
                        }
                    }

                    page++;

                } while (users.size() != 0 && page < 2);

                return users;

            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search users: " + te.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseList<User> users) {
            super.onPostExecute(users);

            if (users != null) {
                mModelDataChange.update(users);
            }

        }
    }
}
