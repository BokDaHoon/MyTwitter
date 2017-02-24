package com.boostcamp.mytwitter.mytwitter.base.db;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class StatusMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            /*RealmObjectSchema alarmSchema = schema.get("StatusRealmObject");
            alarmSchema
                    .removeField("tweetUserId")
                    .addField("tweetUserId", String.class);*/

            RealmObjectSchema tweetSchema = schema.get("TweetRealmObject");
            tweetSchema
            .addField("scheduleDate", Date.class);

            oldVersion++;
        }

    }
}