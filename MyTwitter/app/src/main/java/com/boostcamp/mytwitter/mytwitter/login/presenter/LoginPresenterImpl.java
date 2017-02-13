package com.boostcamp.mytwitter.mytwitter.login.presenter;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.login.model.LoginModel;
import twitter4j.auth.RequestToken;


/**
 * Created by DaHoon on 2017-02-09.
 */

public class LoginPresenterImpl implements LoginPresenter.Presenter {

    private LoginPresenter.View view;
    private LoginModel model;
    private String TAG = "LoginPresentImpl";

    @Override
    public void setView(LoginPresenter.View view) {
        this.view = view;
        model = new LoginModel();
    }

    @Override
    public void getRequestToken() {
        if (TwitterInfo.TwitLogin) {
            view.moveToAuthorityView(TwitterInfo.TwitRequestToken);

        } else {
            new RequestTokenThreadTask().execute();

        }
    }

    @Override
    public void getAccessToken(Intent resultIntent) {
        new AccessTokenThreadTask().execute(resultIntent);
    }

    class RequestTokenThreadTask extends AsyncTask<Void, Void, RequestToken> {

        @Override
        protected RequestToken doInBackground(Void... params) {
            return model.getRequestToken();
        }

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            super.onPostExecute(requestToken);
            if(requestToken != null) {
                view.moveToAuthorityView(requestToken);
            }

        }
    }

    class AccessTokenThreadTask extends AsyncTask<Intent, Void, Void> {

        @Override
        protected Void doInBackground(Intent... params) {
            model.getAccessTokenModel(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.displayConnectSuccess();
            view.moveToTimeLine();
        }
    }
}
