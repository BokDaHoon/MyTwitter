package com.boostcamp.mytwitter.mytwitter.search;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.SharedPreferenceHelper;
import com.boostcamp.mytwitter.mytwitter.base.SingleFragmentActivity;
import com.boostcamp.mytwitter.mytwitter.search.adapter.ViewPagerAdapter;
import com.boostcamp.mytwitter.mytwitter.searchresult.SearchResultActivity;
import com.boostcamp.mytwitter.mytwitter.util.Define;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class SearchActivity extends AppCompatActivity implements MaterialTabListener {


    ImageButton actionbarSearch;

    EditText actionbarEdittxt;

    private MaterialTabHost tabHost;
    private ViewPager pager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferenceHelper.getInstance(this).loadProperties();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Custom Actionbar Setting
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.actionbar_view);
        actionbarEdittxt = (EditText) actionbar.getCustomView().findViewById(R.id.actionbar_edittxt);
        actionbarSearch = (ImageButton) actionbar.getCustomView().findViewById(R.id.actionbar_search);

        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setElevation(0); // Actionbar shadow remove

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

        actionbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearchResult();
            }
        });

        tabbarSetting();
    }

    void moveToSearchResult() {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(Define.SEARCH_WORD_KEY, actionbarEdittxt.getText().toString());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void tabbarSetting() {
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager );

        // init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
}
