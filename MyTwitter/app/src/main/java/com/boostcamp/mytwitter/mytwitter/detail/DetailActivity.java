package com.boostcamp.mytwitter.mytwitter.detail;

import android.content.ContentValues;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.MyTwitterApplication;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.base.db.StatusRealmObject;
import com.boostcamp.mytwitter.mytwitter.base.db.TwitterSchema;
import com.boostcamp.mytwitter.mytwitter.detail.presenter.DetailPresenter;
import com.boostcamp.mytwitter.mytwitter.detail.presenter.DetailPresenterImpl;
import com.boostcamp.mytwitter.mytwitter.timeline.TimelineActivity;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.TimelineAdapter;
import com.boostcamp.mytwitter.mytwitter.util.Define;
import com.boostcamp.mytwitter.mytwitter.write.WriteActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.victor.loading.rotate.RotateLoading;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
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
    @BindView(R.id.detail_tweet_scrap_button)
    ImageButton detailTweetScrapButton;
    @BindView(R.id.progress_bar)
    RotateLoading progressBar;

    private RequestManager mGlideRequestManager;
    private String profileImagePath;
    private MediaEntity[] mediaResult;
    private String metaOgImageUrl;
    private long tweetId;
    private boolean firstFavoriteFlag; // 처음 상태표시를 위한 좋아요 버튼 체크 여부는 무시하기 위한 Flag
    private Status status;


    @Override
    public void onBackPressed() {
        presenter.setFinish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ACTIVITY_TITLE);
        ButterKnife.bind(this);
        mGlideRequestManager = Glide.with(this);

        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void init() {
        adapter = new TimelineAdapter(this);
        presenter = new DetailPresenterImpl();
        presenter.setView(this);
        presenter.setTimelineListAdapterView(adapter);
        presenter.setTimelineListAdapterModel(adapter);
        firstFavoriteFlag = true;

        // 좋아요 버튼 리스너
        detailFavoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 처음 좋아요 버튼 체크 여부는 무시
                if (firstFavoriteFlag) {
                    firstFavoriteFlag = false;
                    return;
                }

                if (isChecked) { // 처음 셋팅 좋아요 버튼 클릭은 제외시키기.
                    new FavoriteTask().execute(true); // 좋아요 표시
                } else {
                    new FavoriteTask().execute(false); // 좋아요 표시 해제
                }

            }
        });

        detailTweetReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToReply(status.getId());
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TimelineActivity.DETAIL_STATUS_KEY);
        status = (Status) bundle.getSerializable(TimelineActivity.DETAIL_STATUS_KEY);
        setDetailView(status);

        tweetId = status.getId();

        // ViewHolder의 Type에 따라 레이아웃을 변경해준다.
        int viewHolderType = intent.getIntExtra(TimelineActivity.VIEWHOLDER_TYPE, -1);

        switch (viewHolderType) {
            case Define.TIMELINE_COMMON_TYPE :
                detailTweetImage.setVisibility(View.GONE);
                detailTwitterCard.setVisibility(View.GONE);
                break;
            case Define.TIMELINE_IMAGE_TYPE :
                detailTweetImage.setVisibility(View.VISIBLE);
                detailTwitterCard.setVisibility(View.GONE);
                setTweetImage(status);
                break;
            case Define.TIMELINE_CARDVIEW_TYPE :
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

    public void moveToReply(long statusId) {
        Intent intent = new Intent(this, WriteActivity.class);
        intent.putExtra("ReplyFlag", true);
        intent.putExtra(Define.TWEET_ID_KEY, statusId);
        startActivity(intent);
    }

    //Detail Activity 기본 설정.
    void setDetailView(Status status) {
        User user = status.getUser();

        profileImagePath = user.getProfileImageURL();
        // 작성자 Profile 사진 표시.
        detailProfileImage.post(new Runnable() {
            @Override
            public void run() {
                mGlideRequestManager
                        .load(profileImagePath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(detailProfileImage);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

        detailRetweetCount.setText(status.getRetweetCount() + "");
        detailFavoriteCount.setText(status.getFavoriteCount() + "");


        if (status.isFavorited()) {
           detailFavoriteButton.setChecked(true);
        } else {
            detailFavoriteButton.setChecked(false);
            firstFavoriteFlag = false;
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
        mediaResult = status.getMediaEntities();
        detailTweetImage.post(new Runnable() {
            @Override
            public void run() {
                mGlideRequestManager
                    .load(mediaResult[0].getMediaURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detailTweetImage);
            }
        });

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
            presenter.setFinish();
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

    @Override
    public boolean isFinishingActivity() {
        return isFinishing();
    }

    // 좋아요를 위한 AsyncTask
    class FavoriteTask extends AsyncTask<Boolean, Void, Status> {

        private Twitter mTwit;
        private int count;

        @Override
        protected twitter4j.Status doInBackground(Boolean... params) {
            mTwit = TwitterInfo.TwitInstance;
            boolean favoriteFlag = params[0];

            if (favoriteFlag) {
                favoriteFunc();
            } else {
                destroyFavoriteFunc();
            }

            twitter4j.Status result = null;
            try {
                result = mTwit.showStatus(tweetId);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return result;
        }

        private void favoriteFunc() {
            try {
                count = Integer.valueOf(detailFavoriteCount.getText().toString()) + 1;
                mTwit.createFavorite(tweetId);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

        private void destroyFavoriteFunc() {
            try {
                count = Integer.valueOf(detailFavoriteCount.getText().toString()) - 1;
                mTwit.destroyFavorite(tweetId);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(twitter4j.Status status) {
            super.onPostExecute(status);
            MyTwitterApplication.getTwitterApplication().notifyObservers(status);
            detailFavoriteCount.setText(count + "");
        }
    }

    private void displaySuccessScrap() {
        Toast.makeText(this, "성공적으로 스크랩 하였습니다.", Toast.LENGTH_SHORT).show();
    }

    private void displayDuplicatedScrap() {
        Toast.makeText(this, "이미 스크랩한 트윗입니다.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.detail_tweet_scrap_button)
    void clickScrapBtn() {
        new ScrapTask().execute();
    }

    class ScrapTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            ContentValues cv = getContentValues();

            if (!isDuplicatedTweet()) { // 중복체크
                getContentResolver().insert(TwitterSchema.CONTENT_URI, cv);
                return false;

            } else {
                return true;

            }

        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            boolean isDupl = flag;

            if (isDupl) {
                displayDuplicatedScrap();
            } else {
                displaySuccessScrap();
            }

            finish();
        }

        private boolean isDuplicatedTweet() {
            long tweetId = status.getId();
            Realm realm = Realm.getDefaultInstance();
            RealmResults<StatusRealmObject> results = realm.where(StatusRealmObject.class).equalTo("tweetId", tweetId).findAll();

            if (results.size() > 0) {
                Log.d("dd", "중복O");
                return true;
            } else {
                Log.d("dd", "중복X");
                return false;
            }

        }

        /**
         * 스크랩 시 반영해야할 데이터를
         * ContentValues에 담아서 리턴해준다.
         * @return 변경 혹은 추가 내용 사항을 ContentValues의 형태로 넘긴다.
         */
        private ContentValues getContentValues(){
            ContentValues cv = new ContentValues();

            // 작성일
            Date temp = status.getCreatedAt();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createdAt = transFormat.format(temp);
            Log.d("날짜 저장", createdAt);

            // 뷰홀더 타입
            int viewHolderType = getIntent().getIntExtra(TimelineActivity.VIEWHOLDER_TYPE, -1);

            cv.put(TwitterSchema.COLUMN_TWEET_ID, status.getId());
            cv.put(TwitterSchema.COLUMN_PROFILE_IMAGE_URL, status.getUser().getProfileImageURL());
            cv.put(TwitterSchema.COLUMN_TWEET_USER_ID, status.getUser().getScreenName());
            cv.put(TwitterSchema.COLUMN_TWEET_USER_NAME, status.getUser().getName());
            cv.put(TwitterSchema.COLUMN_TEXT, status.getText());
            cv.put(TwitterSchema.COLUMN_CREATED_AT, createdAt);
            
            if (viewHolderType == Define.TIMELINE_IMAGE_TYPE && status.getMediaEntities() != null) {
                cv.put(TwitterSchema.COLUMN_IMAGE_URL, status.getMediaEntities()[0].getMediaURL());
            } else {
                cv.put(TwitterSchema.COLUMN_IMAGE_URL, "");
            }

            if (viewHolderType == Define.TIMELINE_CARDVIEW_TYPE && status.getURLEntities() != null) {
                cv.put(TwitterSchema.COLUMN_CARDVIEW_URL, status.getURLEntities()[0].getExpandedURL());
            } else {
                cv.put(TwitterSchema.COLUMN_CARDVIEW_URL, "");
            }

            cv.put(TwitterSchema.COLUMN_TWEET_VIEW_TYPE, viewHolderType);

            return cv;
        }
    }

    /**
     * AsyncTask 모듈들
     */

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

            // 접속 실패 시
            if (doc == null) {
                detailOgImage.setImageResource(R.drawable.twitter_card_default);
                detailOgTitle.setText("Title");
                detailOgUrl.setText(url);
                return;
            }

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
                metaOgImageUrl = metaOgImage.attr("content");

                detailOgImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mGlideRequestManager
                                .load(metaOgImageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .crossFade()
                                .into(detailOgImage);
                    }
                });

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