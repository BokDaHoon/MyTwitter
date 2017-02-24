package com.boostcamp.mytwitter.mytwitter.base.db;

import android.net.Uri;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class TwitterSchema {

    public static final String AUTHORITY = "com.boostcamp.mytwitter.mytwitter";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASK = "task";
    public static final String SEARCH_TASK = "search";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

    public static final String COLUMN_TWEET_ID = "tweetId";
    public static final String COLUMN_PROFILE_IMAGE_URL = "profileImageUrl";
    public static final String COLUMN_TWEET_USER_ID = "tweetUserId";
    public static final String COLUMN_TWEET_USER_NAME = "tweetUserName";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_IMAGE_URL = "imageUrl";
    public static final String COLUMN_CARDVIEW_URL = "cardviewUrl";
    public static final String COLUMN_TWEET_VIEW_TYPE = "tweetViewType";


    public static final String[] sColumns = new String[] {
            "tweetId",
            "profileImageUrl",
            "tweetUserId",
            "tweetUserName",
            "text",
            "createdAt",
            "imageUrl",
            "cardviewUrl",
            "tweetViewType"
    };
}
