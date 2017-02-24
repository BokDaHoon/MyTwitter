package com.boostcamp.mytwitter.mytwitter.write.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnScheduledTweetListener;
import com.boostcamp.mytwitter.mytwitter.scrap.model.SearchDTO;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class TweetSelectDialog extends Dialog implements View.OnFocusChangeListener {

    private FButton scheduledTweetBtn;
    private MaterialEditText tweetDateSearch;
    private MaterialEditText tweetTimeSearch;

    private DatePickerDialog tweetDatePickerDialog;
    private TimePickerDialog tweetTimePickerDialog;

    private SimpleDateFormat dateFormatter;

    private OnScheduledTweetListener mOnClickListener;
    private Context mContext;
    private SearchDTO searchDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_tweet_select);

        scheduledTweetBtn = (FButton) findViewById(R.id.tweet_schedule_btn);
        tweetDateSearch = (MaterialEditText) findViewById(R.id.tweet_date);
        tweetTimeSearch = (MaterialEditText) findViewById(R.id.tweet_time);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        // 클릭 이벤트 셋팅
        scheduledTweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    String date = tweetDateSearch.getText().toString();
                    String time = tweetTimeSearch.getText().toString() + ":00";

                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    Date tweetDate = null;
                    try {
                        tweetDate = transFormat.parse(date + " " + time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mOnClickListener.onItemClick(tweetDate);
                }
            }
        });

        setDateTimeField();
    }

    void setDateTimeField() {
        tweetDateSearch.setOnFocusChangeListener(this);
        tweetTimeSearch.setOnFocusChangeListener(this);

        Calendar newCalendar = Calendar.getInstance();

        // 시작날짜 다이얼로그
        tweetDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tweetDateSearch.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        tweetTimePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour = String.format("%02d", hourOfDay);
                String min = String.format("%02d", minute);
                tweetTimeSearch.setText(hour + ":" + min);
            }
        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), false);
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public TweetSelectDialog(Context context, String title, OnScheduledTweetListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        searchDTO = new SearchDTO();
        this.mOnClickListener = singleListener;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view == tweetDateSearch && hasFocus) {
            tweetDatePickerDialog.show();
            view.clearFocus();
        } else if (view == tweetTimeSearch && hasFocus) {
            tweetTimePickerDialog.show();
            view.clearFocus();
        }
    }
}