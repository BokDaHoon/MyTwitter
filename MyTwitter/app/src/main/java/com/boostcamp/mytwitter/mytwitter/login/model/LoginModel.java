package com.boostcamp.mytwitter.mytwitter.login.model;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by DaHoon on 2017-02-09.
 */

public class LoginModel {

    public void resetTwitInstance() {

        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();

            builder.setOAuthAccessToken(TwitterInfo.TWIT_KEY_TOKEN);
            builder.setOAuthAccessTokenSecret(TwitterInfo.TWIT_KEY_TOKEN_SECRET);
            builder.setOAuthConsumerKey(TwitterInfo.TWIT_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterInfo.TWIT_CONSUMER_SECRET);

            Configuration config = builder.build();
            TwitterFactory tFactory = new TwitterFactory(config);
            TwitterInfo.TwitInstance = tFactory.getInstance();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public RequestToken getRequestToken() {
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setDebugEnabled(true);
            builder.setOAuthConsumerKey(TwitterInfo.TWIT_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterInfo.TWIT_CONSUMER_SECRET);

            TwitterFactory factory = new TwitterFactory(builder.build());
            Twitter mTwit = factory.getInstance();
            final RequestToken mRequestToken = mTwit.getOAuthRequestToken();
            String outToken = mRequestToken.getToken();
            String outTokenSecret = mRequestToken.getTokenSecret();

            TwitterInfo.TwitInstance = mTwit;
            TwitterInfo.TwitRequestToken = mRequestToken;

            return mRequestToken;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public void getAccessTokenModel(Intent resultIntent) {
        try {
            Twitter mTwit = TwitterInfo.TwitInstance;

            AccessToken mAccessToken = mTwit.getOAuthAccessToken(TwitterInfo.TwitRequestToken, resultIntent.getStringExtra("oauthVerifier"));

            TwitterInfo.TwitLogin = true;
            TwitterInfo.TWIT_KEY_TOKEN = mAccessToken.getToken();
            TwitterInfo.TWIT_KEY_TOKEN_SECRET = mAccessToken.getTokenSecret();

            TwitterInfo.TwitAccessToken = mAccessToken;

            TwitterInfo.TwitScreenName = mTwit.getScreenName();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
