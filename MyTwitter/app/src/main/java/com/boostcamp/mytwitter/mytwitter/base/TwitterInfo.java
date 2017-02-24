package com.boostcamp.mytwitter.mytwitter.base;

import java.text.SimpleDateFormat;

import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by DaHoon on 2017-02-09.
 */

public class TwitterInfo {

    public static final String TWIT_API_KEY = "3AyuRwmWaCdyy0wDnZ5Ar3rrK";
    public static final String TWIT_CONSUMER_KEY = "3AyuRwmWaCdyy0wDnZ5Ar3rrK";
    public static final String TWIT_CONSUMER_SECRET = "RvlHIbMrbjHrUZS0c89aMMOmlFa2HOJ3yOwgKyzlSnv1her2PQ";
    public static final String TWIT_CALLBACK_URL = "http://mytwitter.boostcamp.com";
    public static User TwitUser = null;

    public static final int REQ_CODE_TWIT_LOGIN = 1001;

    public static boolean TwitLogin = false;
    public static Twitter TwitInstance = null;
    public static AccessToken TwitAccessToken = null;
    public static RequestToken TwitRequestToken = null;
    public static ConfigurationBuilder builder = null;

    public static String TWIT_KEY_TOKEN = "828798997775081472-Km5yissU0J6HdtjdKYKsabfLokjsBPz";
    public static String TWIT_KEY_TOKEN_SECRET = "X7uitnMoA17SiMbtIvm0BKBJeyv89AXvnCBfDArNgnpeG";
    public static String TwitScreenName = "";

    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

}
