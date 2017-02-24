package com.boostcamp.mytwitter.mytwitter.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by DaHoon on 2017-01-27.
 */

public class AlarmManagerUtil {

    private static AlarmManagerUtil mAlarmManagerUtil;
    private static Context mContext;


    public static AlarmManagerUtil from(Context context) {
        if(mAlarmManagerUtil == null){
            mAlarmManagerUtil = new AlarmManagerUtil();
        }

        if(mContext != context){
            mContext = context;
        }

        return mAlarmManagerUtil;
    }


    public void setAlarm(Date date, PendingIntent alarmPendingIntent) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        setOnceAlarm(date, alarmPendingIntent, alarmManager);
    }


    private void setOnceAlarm(Date date, PendingIntent alarmPendingIntent, AlarmManager alarmManager) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(date.getTime()
                    , alarmPendingIntent), alarmPendingIntent);

        }
        else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    date.getTime(), alarmPendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    date.getTime(), alarmPendingIntent);
        }
    }

    public void unregisterAlarm(PendingIntent alarmPendingIntent){
        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        mAlarmManager.cancel(alarmPendingIntent);
    }
}
