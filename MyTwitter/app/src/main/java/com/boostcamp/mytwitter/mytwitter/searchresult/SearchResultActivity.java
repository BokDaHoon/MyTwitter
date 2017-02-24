package com.boostcamp.mytwitter.mytwitter.searchresult;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.SharedPreferenceHelper;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.profile.ProfileActivity;
import com.boostcamp.mytwitter.mytwitter.search.SearchActivity;
import com.boostcamp.mytwitter.mytwitter.searchresult.adapter.SearchResultAdapter;
import com.boostcamp.mytwitter.mytwitter.searchresult.presenter.SearchResultPresenter;
import com.boostcamp.mytwitter.mytwitter.searchresult.presenter.SearchResultPresenterImpl;
import com.boostcamp.mytwitter.mytwitter.util.Define;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class SearchResultActivity extends AppCompatActivity implements SearchResultPresenter.View {

    EditText actionbarEdittxt;

    @BindView(R.id.actionbar_search)
    ImageButton actionbarSearch;
    @BindView(R.id.search_result_list)
    RecyclerView searchResultList;

    private SearchResultPresenterImpl presenter;
    private SearchResultAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferenceHelper.getInstance(this).loadProperties();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // Custom Actionbar Setting
        ActionBar actionbar = getSupportActionBar();
        actionbar.setCustomView(R.layout.actionbar_view);
        actionbarEdittxt = (EditText) actionbar.getCustomView().findViewById(R.id.actionbar_edittxt);
        actionbarSearch = (ImageButton) actionbar.getCustomView().findViewById(R.id.search_result_list);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        actionbar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        init();
    }

    void init() {
        actionbarEdittxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(SearchResultActivity.this, "Search triggered", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        actionbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    loadSearchResult();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new SearchResultAdapter(this);
        searchResultList.setLayoutManager(layoutManager);
        searchResultList.setAdapter(adapter);

        presenter = new SearchResultPresenterImpl();
        presenter.setView(this);
        presenter.setSearchListAdapterView(adapter);
        presenter.setSearchListAdapterModel(adapter);

        // 검색어를 입력했다면
        Intent intent = getIntent();
        String word = intent.getStringExtra(Define.SEARCH_WORD_KEY);
        if (!word.equals("")) {
            try {
                actionbarEdittxt.setText(word);
                presenter.loadSearchResult(word);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    void loadSearchResult() throws ExecutionException, InterruptedException {
        String inputName = actionbarEdittxt.getText().toString();
        presenter.loadSearchResult(inputName);
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

    @Override
    public void moveToProfile(int position) {
        long id = adapter.getListData(position).getId();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("ProfileFlag", Define.OTHER_PROFILE);
        intent.putExtra(Define.USER_ID_KEY, id);
        startActivity(intent);
    }
}
