package com.boostcamp.mytwitter.mytwitter.base.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import twitter4j.Status;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-21.
 */
@RealmClass
public class StatusRealmObject extends RealmObject {
    @PrimaryKey
    private long tweetId;

    private String profileImageUrl;

    private String tweetUserId;

    private String tweetUserName;

    private String text;

    private Date createdAt;

    private String imageUrl;

    private String cardviewUrl;

    private int tweetViewType;

    public long getTweetId() {
        return tweetId;
    }

    public StatusRealmObject setTweetId(long tweetId) {
        this.tweetId = tweetId;
        return this;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public StatusRealmObject setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public String getTweetUserId() {
        return tweetUserId;
    }

    public StatusRealmObject setTweetUserId(String tweetUserId) {
        this.tweetUserId = tweetUserId;
        return this;
    }

    public String getTweetUserName() {
        return tweetUserName;
    }

    public StatusRealmObject setTweetUserName(String tweetUserName) {
        this.tweetUserName = tweetUserName;
        return this;
    }

    public String getText() {
        return text;
    }

    public StatusRealmObject setText(String text) {
        this.text = text;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public StatusRealmObject setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public StatusRealmObject setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getCardviewUrl() {
        return cardviewUrl;
    }

    public StatusRealmObject setCardviewUrl(String cardviewUrl) {
        this.cardviewUrl = cardviewUrl;
        return this;
    }

    public int getTweetViewType() {
        return tweetViewType;
    }

    public StatusRealmObject setTweetViewType(int tweetViewType) {
        this.tweetViewType = tweetViewType;
        return this;
    }
}
