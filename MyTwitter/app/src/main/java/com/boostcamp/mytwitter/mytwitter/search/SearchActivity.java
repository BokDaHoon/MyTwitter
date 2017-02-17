package com.boostcamp.mytwitter.mytwitter.search;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.SingleFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends SingleFragmentActivity {

    @BindView(R.id.actionbar_edittxt)
    EditText actionbarEdittxt;
    @BindView(R.id.actionbar_search)
    ImageButton actionbarSearch;

    @Override
    protected Fragment createFragment() {
        return new NaverSearchFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Custom Actionbar Setting
        ActionBar actionbar = getSupportActionBar();
        actionbar.setCustomView(R.layout.actionbar_view);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        actionbar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        init();
    }

    void init() {
        actionbarEdittxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(SearchActivity.this, "Search triggered", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
