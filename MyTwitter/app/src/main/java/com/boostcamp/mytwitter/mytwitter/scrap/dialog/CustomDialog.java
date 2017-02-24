package com.boostcamp.mytwitter.mytwitter.scrap.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnSearchClickListener;
import com.boostcamp.mytwitter.mytwitter.scrap.model.SearchDTO;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class CustomDialog extends Dialog implements View.OnFocusChangeListener {

    private FButton scrapSearchBtn;
    private MaterialEditText contentSearch;
    private MaterialEditText startDateSearch;
    private MaterialEditText endDateSearch;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private OnSearchClickListener mOnClickListener;
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

        setContentView(R.layout.dialog_scrap);

        scrapSearchBtn = (FButton) findViewById(R.id.scrap_search_btn);
        contentSearch = (MaterialEditText) findViewById(R.id.search_content);
        startDateSearch = (MaterialEditText) findViewById(R.id.search_start_date);
        endDateSearch = (MaterialEditText) findViewById(R.id.search_end_date);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        // 클릭 이벤트 셋팅
        scrapSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    String start = startDateSearch.getText().toString();
                    String end = endDateSearch.getText().toString();

                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = null;
                    Date endDate = null;
                    try {
                        startDate = transFormat.parse(start);
                        endDate = transFormat.parse(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    searchDTO.setContent(contentSearch.getText().toString())
                             .setStartDate(startDate)
                             .setEndDate(endDate);

                    mOnClickListener.onItemClick(searchDTO);
                }
            }
        });

        setDateTimeField();
    }

    void setDateTimeField() {
        startDateSearch.setOnFocusChangeListener(this);
        endDateSearch.setOnFocusChangeListener(this);

        Calendar newCalendar = Calendar.getInstance();

        // 시작날짜 다이얼로그
        startDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDateSearch.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // 종료날짜 다이얼로그
        endDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDateSearch.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public CustomDialog(Context context, String title,
                        OnSearchClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        searchDTO = new SearchDTO();
        this.mOnClickListener = singleListener;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view == startDateSearch && hasFocus) {
            startDatePickerDialog.show();
        } else if (view == endDateSearch && hasFocus) {
            endDatePickerDialog.show();
        }

        view.clearFocus();

    }
}