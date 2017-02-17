package com.boostcamp.mytwitter.mytwitter.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.detail.presenter.DetailPresenter;
import com.boostcamp.mytwitter.mytwitter.detail.presenter.DetailPresenterImpl;
import com.boostcamp.mytwitter.mytwitter.timeline.TimelineActivity;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.TimelineAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.victor.loading.rotate.RotateLoading;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

public class DetailActivity extends AppCompatActivity implements DetailPresenter.View {

    private static final String ACTIVITY_TITLE = "트윗";

    private DetailPresenterImpl presenter;
    private TimelineAdapter adapter;

    @BindView(R.id.detail_reply_list)
    RecyclerView detailReplyList;
    @BindView(R.id.detail_profile_image)
    ImageView detailProfileImage;
    @BindView(R.id.detail_tweet_user_name)
    TextView detailTweetUserName;
    @BindView(R.id.detail_tweet_user_id)
    TextView detailTweetUserId;
    @BindView(R.id.detail_tweet_content)
    TextView detailTweetContent;
    @BindView(R.id.detail_tweet_image)
    ImageView detailTweetImage;
    @BindView(R.id.detail_twitter_card)
    CardView detailTwitterCard;
    @BindView(R.id.detail_og_image)
    ImageView detailOgImage;
    @BindView(R.id.detail_og_title)
    TextView detailOgTitle;
    @BindView(R.id.detail_og_url)
    TextView detailOgUrl;
    @BindView(R.id.detail_created_date_in)
    TextView detailCreatedDateIn;
    @BindView(R.id.detail_created_time_at)
    TextView detailCreatedTimeAt;
    @BindView(R.id.detail_retweet_count)
    TextView detailRetweetCount;
    @BindView(R.id.detail_favorite_count)
    TextView detailFavoriteCount;
    @BindView(R.id.detail_tweet_reply_button)
    ImageButton detailTweetReplyButton;
    @BindView(R.id.detail_favorite_button)
    ToggleButton detailFavoriteButton;
    @BindView(R.id.progress_bar)
    RotateLoading progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ACTIVITY_TITLE);
        ButterKnife.bind(this);

        init();
    }

    void init() {
        adapter = new TimelineAdapter(this);
        presenter = new DetailPresenterImpl();
        presenter.setView(this);
        presenter.setTimelineListAdapterView(adapter);
        presenter.setTimelineListAdapterModel(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TimelineActivity.DETAIL_STATUS_KEY);
        Status status = (Status) bundle.getSerializable(TimelineActivity.DETAIL_STATUS_KEY);
        setDetailView(status);

        // ViewHolder의 Type에 따라 레이아웃을 변경해준다.
        int viewHolderType = intent.getIntExtra(TimelineActivity.VIEWHOLDER_TYPE, -1);
        switch (viewHolderType) {
            case TimelineAdapter.TIMELINE_COMMON_TYPE :
                detailTweetImage.setVisibility(View.GONE);
                detailTwitterCard.setVisibility(View.GONE);
                break;
            case TimelineAdapter.TIMELINE_IMAGE_TYPE :
                detailTweetImage.setVisibility(View.VISIBLE);
                detailTwitterCard.setVisibility(View.GONE);
                setTweetImage(status);
                break;
            case TimelineAdapter.TIMELINE_CARDVIEW_TYPE :
                detailTweetImage.setVisibility(View.GONE);
                detailTwitterCard.setVisibility(View.VISIBLE);
                setTwitterCard(status);
                break;
            default:

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        detailReplyList.setLayoutManager(layoutManager);
        detailReplyList.setAdapter(adapter);
        presenter.loadReplyList(status);
    }

    //Detail Activity 기본 설정.
    void setDetailView(Status status) {
        User user = status.getUser();

        // 작성자 Profile 사진 표시.
        Glide.with(this)
             .load(user.getProfileImageURL())
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .into(detailProfileImage);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");
        Log.d("DetailActivity", status.toString());

        detailRetweetCount.setText(status.getRetweetCount() + "");
        detailFavoriteCount.setText(status.getFavoriteCount() + "");

        if (status.isFavorited()) {
           detailFavoriteButton.setChecked(true);
        } else {
            detailFavoriteButton.setChecked(false);
        }

        //작성일 표시.
        detailCreatedDateIn.setText(dateFormat.format(status.getCreatedAt()));
        detailCreatedTimeAt.setText(timeFormat.format(status.getCreatedAt()));

        detailTweetUserName.setText(user.getName()); // 작성자 이름 표시.
        detailTweetUserId.setText("@" + user.getScreenName()); // 작성자 ID 표시.
        detailTweetContent.setText(status.getText()); // 작성 내용 표시.
    }

    // 트윗 이미지 세팅.
    void setTweetImage(Status status) {
        MediaEntity[] mediaResult = status.getMediaEntities();
        Glide.with(this)
                .load(mediaResult[0].getMediaURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detailTweetImage);
    }

    // 트위터 카드의 URL을 Document 형태로 가져오기.
    void setTwitterCard(Status status) {
        String url = status.getURLEntities()[0].getExpandedURL();
        new SetCardViewTask(this).execute(url);
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
    public void moveToDetail(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TimelineActivity.DETAIL_STATUS_KEY, adapter.getListData(position));
        intent.putExtra(TimelineActivity.VIEWHOLDER_TYPE, adapter.getItemViewType(position));
        intent.putExtra(TimelineActivity.DETAIL_STATUS_KEY, bundle);
        startActivity(intent);
    }

    @Override
    public void progressStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void progressStop() {
        progressBar.setVisibility(View.GONE);
    }

    // TwitterCard UI를 세팅하기 위한 AsnycTask
    class SetCardViewTask extends AsyncTask<String, Void, Document> {

        private String url = "";
        private Context mContext;


        public SetCardViewTask(Context context) {
            mContext = context;
        }

        @Override
        protected Document doInBackground(String... params) {

            url = params[0];

            Connection conn = Jsoup.connect(url);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            super.onPostExecute(doc);

            // TwitterCard 제목 설정
            Elements metaOgTitle = doc.select("meta[property=og:title]");
            if (metaOgTitle.attr("content") != "") {
                detailOgTitle.setText(metaOgTitle.attr("content"));
            }
            else {
                detailOgTitle.setText(doc.title());
            }


            // TwitterCard URL 설정
            Elements metaOgUrl = doc.select("meta[property=og:url]");
            if (metaOgUrl.attr("content") != "") {
                detailOgUrl.setText(metaOgUrl.attr("content"));
            }
            else {
                detailOgUrl.setText(url);
            }


            // TwitterCard Image 설정
            String imageUrl = null;
            Elements metaOgImage = doc.select("meta[property=og:image]");

            if (metaOgImage.attr("content") != "") {
                imageUrl = metaOgImage.attr("content");
                Glide.with(mContext)
                     .load(imageUrl)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .crossFade()
                     .into(detailOgImage);
            } else {
                detailOgImage.setImageResource(R.drawable.twitter_card_default);
            }

            // TwitterCard Link 설정
            detailTwitterCard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                }
            });

        }

    }
}