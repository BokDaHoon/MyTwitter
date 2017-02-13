package com.boostcamp.mytwitter.mytwitter.base;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class MyTwitterApiClient extends TwitterApiClient {

    interface CustomService {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        @GET("/1.1/users/show.json")
        Call<User> show(@Query("user_id") long id);
    }

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public CustomService getCustomService() {
        return getService(CustomService.class);
    }
}

