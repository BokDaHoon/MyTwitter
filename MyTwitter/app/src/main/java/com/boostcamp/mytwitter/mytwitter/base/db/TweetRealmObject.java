package com.boostcamp.mytwitter.mytwitter.base.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DaHoon on 2017-02-24.
 */

public class TweetRealmObject extends RealmObject {
    @PrimaryKey
    private long id;

    private String text;

    private String imagePath;

    private boolean imageFlag;

    private boolean replyFlag;

    private Date scheduleDate;

    public long getId() {
        return id;
    }

    public TweetRealmObject setId(long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public TweetRealmObject setText(String text) {
        this.text = text;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public TweetRealmObject setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public boolean isImageFlag() {
        return imageFlag;
    }

    public TweetRealmObject setImageFlag(boolean imageFlag) {
        this.imageFlag = imageFlag;
        return this;
    }


    public boolean isReplyFlag() {
        return replyFlag;
    }

    public TweetRealmObject setReplyFlag(boolean replyFlag) {
        this.replyFlag = replyFlag;
        return this;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public TweetRealmObject setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }
}
