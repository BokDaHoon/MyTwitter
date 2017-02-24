package com.boostcamp.mytwitter.mytwitter.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class SharedPreferenceHelper{
    private static final String SETTINGS_NAME = "TWIT";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferenceHelper mHelper;
    private static Context mContext;
    private SharedPreferences.Editor mEditor;

    private SharedPreferenceHelper() {
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if(mHelper == null) {
            mContext = context;
            mHelper = new SharedPreferenceHelper();
        }

        return mHelper;
    }

    public static SharedPreferences getSharedPreferences(Context context){
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences;
    }

    public void saveProperties() {
        mSharedPreferences = mContext.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mEditor.putBoolean("TwitLogin", TwitterInfo.TwitLogin);
        mEditor.putString("TWIT_KEY_TOKEN", TwitterInfo.TWIT_KEY_TOKEN);
        mEditor.putString("TWIT_KEY_TOKEN_SECRET", TwitterInfo.TWIT_KEY_TOKEN_SECRET);

        mEditor.commit();
    }

    public void loadProperties() {
        mSharedPreferences = mContext.getSharedPreferences(SETTINGS_NAME, mContext.MODE_PRIVATE);

        TwitterInfo.TwitLogin = mSharedPreferences.getBoolean("TwitLogin", false);
        TwitterInfo.TWIT_KEY_TOKEN = mSharedPreferences.getString("TWIT_KEY_TOKEN", "");
        TwitterInfo.TWIT_KEY_TOKEN_SECRET = mSharedPreferences.getString("TWIT_KEY_TOKEN_SECRET", "");
    }

    public void clearProperties() {
        mSharedPreferences = mContext.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
    }
}