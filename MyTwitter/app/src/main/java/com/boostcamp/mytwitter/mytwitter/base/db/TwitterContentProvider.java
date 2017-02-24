package com.boostcamp.mytwitter.mytwitter.base.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.twitter.sdk.android.Twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class TwitterContentProvider extends ContentProvider {
    public static final int TASK_ALL = 100;
    public static final int TASK_WITH_ID = 101;
    public static final int SEARCH_WITH_CONTENT = 102;
    public static final int SEARCH_WITH_DATE = 103;
    public static final int SEARCH_WITH_ALL = 104;
    public static final int SEARCH_ALL = 105;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Realm 관련 Class
    private Realm realm;
    private RealmQuery<StatusRealmObject> query;
    private RealmResults<StatusRealmObject> results;
    private MatrixCursor cursor;

    /**
     * path와 int형 task를 1:1 mapping
     * @return 설정된 uriMathcer 반환
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TwitterSchema.AUTHORITY, TwitterSchema.PATH_TASK, TASK_ALL);
        uriMatcher.addURI(TwitterSchema.AUTHORITY, TwitterSchema.PATH_TASK + "/#", TASK_WITH_ID);

        // Search URI
        uriMatcher.addURI(TwitterSchema.AUTHORITY, TwitterSchema.SEARCH_TASK, SEARCH_ALL);
        uriMatcher.addURI(TwitterSchema.AUTHORITY, TwitterSchema.SEARCH_TASK + "/content/*", SEARCH_WITH_CONTENT);
        uriMatcher.addURI(TwitterSchema.AUTHORITY, TwitterSchema.SEARCH_TASK + "/from/*/to/*", SEARCH_WITH_DATE);
        uriMatcher.addURI(TwitterSchema.AUTHORITY, TwitterSchema.SEARCH_TASK + "/content/*/from/*/to/*", SEARCH_WITH_ALL);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        realm = Realm.getDefaultInstance();

        cursor = new MatrixCursor(TwitterSchema.sColumns);

        int match = sUriMatcher.match(uri);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        switch(match){
            case SEARCH_ALL :
                query = realm.where(StatusRealmObject.class);
                results = query.findAllSorted("createdAt", Sort.DESCENDING);
                cursor =  setMatrixCursor(results);

                break;

            case TASK_WITH_ID :
                long id = Long.valueOf(uri.getPathSegments().get(1));

                query = realm.where(StatusRealmObject.class).equalTo("id", id);
                StatusRealmObject status = query.findFirst();

                Object[] rowData = new Object[]{
                        status.getTweetId(),
                        status.getProfileImageUrl(),
                        status.getTweetUserId(),
                        status.getTweetUserName(),
                        status.getText(),
                        status.getCreatedAt(),
                        status.getImageUrl(),
                        status.getCardviewUrl(),
                        status.getTweetViewType()
                };
                cursor.addRow(rowData);
                break;

            case SEARCH_WITH_CONTENT :
                String content = uri.getPathSegments().get(2);

                query = realm.where(StatusRealmObject.class).contains("text", content);
                results = query.findAll();
                cursor = setMatrixCursor(results);

                break;

            case SEARCH_WITH_DATE :
                String fromDateParam = uri.getPathSegments().get(2);
                String toDateParam = uri.getPathSegments().get(4);

                Date fromDate = null;
                Date toDate = null;

                try {
                    fromDate = transFormat.parse(fromDateParam);
                    toDate = transFormat.parse(toDateParam);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                query = realm.where(StatusRealmObject.class).between("createdAt", fromDate, toDate);
                results = query.findAll();
                cursor = setMatrixCursor(results);

                break;

            case SEARCH_WITH_ALL :
                String contentParamAll = uri.getPathSegments().get(2);
                String fromDateParamAll = uri.getPathSegments().get(4);
                String toDateParamAll = uri.getPathSegments().get(6);

                Date fromDateAll = null;
                Date toDateAll = null;

                try {
                    fromDateAll = transFormat.parse(fromDateParamAll);
                    toDateAll = transFormat.parse(toDateParamAll);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                query = realm.where(StatusRealmObject.class).contains("text", contentParamAll)
                                                            .between("createdAt", fromDateAll, toDateAll);
                results = query.findAll();
                cursor = setMatrixCursor(results);

                break;
        }

        realm.close();
        return cursor;
    }

    private MatrixCursor setMatrixCursor(RealmResults<StatusRealmObject> results) {
        MatrixCursor resultCursor = new MatrixCursor(TwitterSchema.sColumns);;

        for(StatusRealmObject status : results){

            Date temp = status.getCreatedAt();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createdAt = transFormat.format(temp);

            Object[] rowData = new Object[]{
                    status.getTweetId(),
                    status.getProfileImageUrl(),
                    status.getTweetUserId(),
                    status.getTweetUserName(),
                    status.getText(),
                    createdAt,
                    status.getImageUrl(),
                    status.getCardviewUrl(),
                    status.getTweetViewType()
            };
            resultCursor.addRow(rowData);
        }

        return resultCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv) {
        realm = Realm.getDefaultInstance();

        try {
            dataInsertOrUpdate(realm, cv, false, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Uri.withAppendedPath(uri, String.valueOf(cv.getAsLong(TwitterSchema.COLUMN_TWEET_ID)));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        realm = Realm.getDefaultInstance();

        cursor = new MatrixCursor(TwitterSchema.sColumns);

        int match = sUriMatcher.match(uri);

        switch(match) {
            case TASK_ALL:
                break;
            case TASK_WITH_ID:
                long id = Long.valueOf(uri.getPathSegments().get(1));

                final RealmResults<StatusRealmObject> deleteAlarm = realm.where(StatusRealmObject.class).equalTo("tweetId",
                        Long.valueOf(id)).findAll(); // 삭제할 객체 GET

                realm.beginTransaction();
                deleteAlarm.deleteAllFromRealm(); // 조건에 해당하는 모든 객체 delete
                realm.commitTransaction();
                break;
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
        realm = Realm.getDefaultInstance();

        try {
            dataInsertOrUpdate(realm, cv, true, selection);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 1;
    }

    /**
     * Alarm객체의 내용을 '삽입' or '수정'한다.
     * @param realm : DB작업을 위한 realm객체
     * @param cv    : '삽입' or '수정'하기 위한 내용을 담은 객체
     * @param modifyFlag : 수정 여부
     * @param selection : '수정'시에 수정할 조건문
     */
    private void dataInsertOrUpdate(Realm realm, ContentValues cv, boolean modifyFlag, String selection) throws ParseException {
        realm.beginTransaction();
        StatusRealmObject status = realm.createObject(StatusRealmObject.class, cv.getAsLong(TwitterSchema.COLUMN_TWEET_ID));

        String temp = cv.getAsString(TwitterSchema.COLUMN_CREATED_AT);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAt = transFormat.parse(temp);

        status.setProfileImageUrl(cv.getAsString(TwitterSchema.COLUMN_PROFILE_IMAGE_URL))
              .setTweetUserId(cv.getAsString(TwitterSchema.COLUMN_TWEET_USER_ID))
              .setTweetUserName(cv.getAsString(TwitterSchema.COLUMN_TWEET_USER_NAME))
              .setText(cv.getAsString(TwitterSchema.COLUMN_TEXT))
              .setCreatedAt(createdAt)
              .setImageUrl(cv.getAsString(TwitterSchema.COLUMN_IMAGE_URL))
              .setCardviewUrl(cv.getAsString(TwitterSchema.COLUMN_CARDVIEW_URL))
              .setTweetViewType(cv.getAsInteger(TwitterSchema.COLUMN_TWEET_VIEW_TYPE));

        realm.commitTransaction();
        realm.close();
    }
}
