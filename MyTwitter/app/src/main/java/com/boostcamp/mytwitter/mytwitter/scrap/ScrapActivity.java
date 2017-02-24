package com.boostcamp.mytwitter.mytwitter.scrap;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.SharedPreferenceHelper;
import com.boostcamp.mytwitter.mytwitter.base.db.TwitterSchema;
import com.boostcamp.mytwitter.mytwitter.listener.OnSearchClickListener;
import com.boostcamp.mytwitter.mytwitter.scrap.adapter.ScrapAdapter;
import com.boostcamp.mytwitter.mytwitter.scrap.dialog.CustomDialog;
import com.boostcamp.mytwitter.mytwitter.scrap.model.SearchDTO;
import com.boostcamp.mytwitter.mytwitter.scrap.presenter.ScrapPresenter;
import com.boostcamp.mytwitter.mytwitter.scrap.presenter.ScrapPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrapActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ScrapPresenter.View {

    @BindView(R.id.scrap_list)
    RecyclerView scrapList;

    ImageButton scrapSearch;

    private ScrapAdapter adapter;
    private ScrapPresenterImpl presenter;
    private CustomDialog mCustomDialog;
    private static final int TASK_LOADER_ID = 100;
    private long selectTweetId;

    private static final int ERROR_START_DATE_MORE_THAN = 201;
    private static final int ERROR_NOT_INPUT_START_DATE = 202;
    private static final int ERROR_NOT_INPUT_END_DATE = 203;


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferenceHelper.getInstance(this).loadProperties();

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(TASK_LOADER_ID);
        if(loader == null){
            getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        }else{
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);
        ButterKnife.bind(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setCustomView(R.layout.actionbar_scrap_view);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|
                                    ActionBar.DISPLAY_SHOW_CUSTOM|
                                    ActionBar.DISPLAY_HOME_AS_UP);

        scrapSearch = (ImageButton) actionbar.getCustomView().findViewById(R.id.scrap_search);

        init();
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ScrapAdapter(this);
        scrapList.setLayoutManager(layoutManager);
        scrapList.setAdapter(adapter);

        presenter = new ScrapPresenterImpl();
        presenter.setView(this);
        presenter.setScrapAdapterView(adapter);

        mCustomDialog = new CustomDialog(ScrapActivity.this, "[다이얼로그 제목]", listener);
        scrapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog.show();
            }
        });
    }

    private OnSearchClickListener listener = new OnSearchClickListener() {
        @Override
        public void onItemClick(SearchDTO searchDTO) {
            // 검색이 성공했을 시에
            if (searchScrap(searchDTO)) {
                mCustomDialog.dismiss();
            }
        }

    };

    private boolean searchScrap(SearchDTO searchDTO) {
        if (!validationCheck(searchDTO)) { // 유효성 체크
            return false;
        }

        String content = searchDTO.getContent();
        Date startDate = searchDTO.getStartDate();
        Date endDate = searchDTO.getEndDate();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = "", endDateStr = "";
        if (startDate != null) { startDateStr = transFormat.format(startDate) + " 00:00:00"; }
        if (endDate != null)   { endDateStr = transFormat.format(endDate) + " 24:00:00";     }

        Uri.Builder builder = TwitterSchema.BASE_CONTENT_URI.buildUpon()
                                           .appendPath(TwitterSchema.SEARCH_TASK);

        if (content.length() > 0) {
            builder.appendPath("content")
                   .appendPath(content);
        }

        if (startDate != null && endDate != null) {
            builder.appendPath("from")
                   .appendPath(startDateStr)
                   .appendPath("to")
                   .appendPath(endDateStr);
        }

        Uri queryUri = builder.build();

        new SearchTask().execute(queryUri);
        return true;
    }

    private boolean validationCheck(SearchDTO searchDTO) {
        String content = searchDTO.getContent();
        Date startDate = searchDTO.getStartDate();
        Date endDate = searchDTO.getEndDate();

        // 종료 날짜만 입력한 경우
        if (startDate == null && endDate != null) {
            displayError(ERROR_NOT_INPUT_START_DATE);
            return false;

        // 시작 날짜만 입력한 경우
        } else if (startDate != null && endDate == null) {
            displayError(ERROR_NOT_INPUT_END_DATE);
            return false;
        }

        // 시작날짜가 종료날짜를 앞서는 경우
        if (startDate != null && endDate != null && startDate.compareTo(endDate) > 0) {
            displayError(ERROR_START_DATE_MORE_THAN);
            return false;
        }

        return true;
    }

    private void displaySuccessDelete() {
        Toast.makeText(this, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void displayError(int errorType) {

        switch (errorType) {
            case ERROR_START_DATE_MORE_THAN :
                Toast.makeText(this, "시작날짜가 종료날짜보다 앞섭니다.", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_NOT_INPUT_START_DATE :
                Toast.makeText(this, "시작날짜를 입력하세요.", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_NOT_INPUT_END_DATE :
                Toast.makeText(this, "종료날짜를 입력하세요.", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    @Override
    public void displayDialog(int position) {
        Log.d("", "Click!");
        selectTweetId = adapter.getDataId(position);
        if (selectTweetId == -1) {
            return;
        }

        final CharSequence[] items = {"삭제"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // BackKey눌렀을 때 꺼지도록 셋팅.
        alertDialogBuilder
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog,
                                         int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

        // 제목셋팅
        alertDialogBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switch(id) {
                    case 0 :
                        getContentResolver().delete(ContentUris.withAppendedId(TwitterSchema.CONTENT_URI, selectTweetId),
                                null,
                                null);
                        displaySuccessDelete();
                        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, ScrapActivity.this);
                        break;
                    default :
                        break;
                }

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create(); // 다이얼로그 생성
        alertDialog.show(); // 다이얼로그 보여주기
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SearchTask extends AsyncTask<Uri, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Uri... params) {
            Uri queryUri = params[0];

            Cursor cursor = getContentResolver().query(queryUri,
                                    null,
                                    null,
                                    null,
                                    null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            adapter.swapCursor(cursor);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData;

            @Override
            protected void onStartLoading() {
                if(mTaskData != null){
                    deliverResult(mTaskData);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try{
                    return getContentResolver().query(TwitterSchema.BASE_CONTENT_URI
                                                                   .buildUpon()
                                                                   .appendPath(TwitterSchema.SEARCH_TASK)
                                                                   .build(),
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }catch(Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}
