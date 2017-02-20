package com.boostcamp.mytwitter.mytwitter.timeline.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnProfileItemClickListener;
import com.boostcamp.mytwitter.mytwitter.profile.ProfileActivity;
import com.boostcamp.mytwitter.mytwitter.util.Define;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import twitter4j.URLEntity;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-15.
 */

public class TimelineCardviewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnProfileItemClickListener mProfileItemClickListener;

    @BindView(R.id.writer_profile)
    ImageView writerProfile;
    @BindView(R.id.writer_name)
    TextView writerName;
    @BindView(R.id.writer_id)
    TextView writerId;
    @BindView(R.id.tweet_content)
    TextView tweetContent;
    @BindView(R.id.created_date_in)
    TextView createDateIn;
    @BindView(R.id.created_time_at)
    TextView createTimeAt;
    @BindView(R.id.tweet_reply)
    ImageView tweetReply;
    @BindView(R.id.tweet_favorite)
    ToggleButton tweetFavorite;
    @BindView(R.id.tweet_favorite_count)
    TextView tweetFavoirteCount;
    @BindView(R.id.twitter_card)
    CardView twitterCard;
    @BindView(R.id.og_image)
    ImageView ogTagImage;
    @BindView(R.id.og_title)
    TextView ogTagTitle;
    @BindView(R.id.og_url)
    TextView ogTagUrl;

    private RequestManager mGlideRequestManager;
    private String profileImagePath;
    private String metaOgImageUrl;

    public TimelineCardviewHolder(Context context, View itemView, OnItemClickListener listener, OnProfileItemClickListener profileListener) {
        super(itemView);

        mContext = context;
        mOnItemClickListener = listener;
        mProfileItemClickListener = profileListener;
        mGlideRequestManager = Glide.with(mContext);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final Status status, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        writerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileItemClickListener != null) {
                    mProfileItemClickListener.onProfileItemClick(status.getUser().getId());
                }
            }
        });

        User user = status.getUser();
        writerName.setText(user.getName());
        writerId.setText("@" + user.getScreenName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

        profileImagePath = user.getProfileImageURL();

        writerProfile.post(new Runnable() {
            @Override
            public void run() {
                mGlideRequestManager
                        .load(profileImagePath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(writerProfile);
            }
        });

        if (status.isFavorited()) {
            tweetFavorite.setChecked(true);
        } else {
            tweetFavorite.setChecked(false);
        }


        URLEntity[] urlResult = status.getURLEntities();
        new SetCardViewTask().execute(urlResult[0].getExpandedURL()); // TwitterCard 세팅


        createDateIn.setText(dateFormat.format(status.getCreatedAt()));
        createTimeAt.setText(timeFormat.format(status.getCreatedAt()));
        tweetContent.setText(status.getText());
        tweetFavoirteCount.setText(String.valueOf(status.getFavoriteCount()));
    }

    void moveToProfile(long id) {
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra("ProfileFlag", Define.OTHER_PROFILE);
        intent.putExtra(Define.USER_ID_KEY, id);
        mContext.startActivity(intent);
    }

    // TwitterCard UI를 세팅하기 위한 AsnycTask
    class SetCardViewTask extends AsyncTask<String, Void, Document> {

        private String url = "";

        @Override
        protected Document doInBackground(String... params) {

            url = params[0];
            Log.d("url 확인", url);
            Connection conn = Jsoup.connect(url).timeout(5000);
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
                ogTagImage.setImageResource(R.drawable.twitter_card_default);
                ogTagTitle.setText("Title");
                ogTagUrl.setText(url);
                return;
            }

            // TwitterCard 제목 설정
            Elements metaOgTitle = doc.select("meta[property=og:title]");
            if (metaOgTitle != null && metaOgTitle.attr("content") != "") {
                ogTagTitle.setText(metaOgTitle.attr("content"));
            }
            else {
                ogTagTitle.setText(doc.title());
            }


            // TwitterCard URL 설정
            Elements metaOgUrl = doc.select("meta[property=og:url]");
            if (metaOgUrl != null && metaOgUrl.attr("content") != "") {
                ogTagUrl.setText(metaOgUrl.attr("content"));
            }
            else {
                ogTagUrl.setText(url);
            }


            // TwitterCard Image 설정
            String imageUrl = null;
            Elements metaOgImage = doc.select("meta[property=og:image]");

            if (metaOgImage != null && metaOgImage.attr("content") != "") {
                metaOgImageUrl = metaOgImage.attr("content");
                ogTagImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mGlideRequestManager
                            .load(metaOgImageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .into(ogTagImage);
                    }
                });

            } else {
                ogTagImage.setImageResource(R.drawable.twitter_card_default);
            }

            // TwitterCard Link 설정
            twitterCard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                }
            });

        }

    }
}
