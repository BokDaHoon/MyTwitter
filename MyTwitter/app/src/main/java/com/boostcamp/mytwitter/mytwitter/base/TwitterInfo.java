package com.boostcamp.mytwitter.mytwitter.base;

import java.text.SimpleDateFormat;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by DaHoon on 2017-02-09.
 */

public class TwitterInfo {

    public static final String TWIT_API_KEY = "3AyuRwmWaCdyy0wDnZ5Ar3rrK";
    public static final String TWIT_CONSUMER_KEY = "3AyuRwmWaCdyy0wDnZ5Ar3rrK";
    public static final String TWIT_CONSUMER_SECRET = "RvlHIbMrbjHrUZS0c89aMMOmlFa2HOJ3yOwgKyzlSnv1her2PQ";
    public static final String TWIT_CALLBACK_URL = "http://mytwitter.boostcamp.com";

    public static final int REQ_CODE_TWIT_LOGIN = 1001;

    public static boolean TwitLogin = false;
    public static Twitter TwitInstance = null;
    public static AccessToken TwitAccessToken = null;
    public static RequestToken TwitRequestToken = null;

    public static String TWIT_KEY_TOKEN = "";
    public static String TWIT_KEY_TOKEN_SECRET = "";
    public static String TwitScreenName = "";

    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

}
