package com.boostcamp.mytwitter.mytwitter.scrap.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.db.TwitterSchema;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class ScrapCardviewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.scrap_writer_profile)
    ImageView writerProfile;
    @BindView(R.id.scrap_writer_name)
    TextView writerName;
    @BindView(R.id.scrap_writer_id)
    TextView writerId;
    @BindView(R.id.scrap_tweet_content)
    TextView tweetContent;
    @BindView(R.id.scrap_created_date_in)
    TextView createDateIn;
    @BindView(R.id.scrap_created_time_at)
    TextView createTimeAt;
    @BindView(R.id.scrap_twitter_card)
    CardView twitterCard;
    @BindView(R.id.scrap_og_image)
    ImageView scrapOgTagImage;
    @BindView(R.id.scrap_og_title)
    TextView scrapOgTagTitle;
    @BindView(R.id.scrap_og_url)
    TextView scrapOgTagUrl;
    @BindView(R.id.tweet_option_menu)
    ImageButton tweetOptionMenu;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private RequestManager mGlideRequestManager;
    private String metaOgImageUrl;

    public ScrapCardviewHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        mContext = context;
        mOnItemClickListener = listener;
        mGlideRequestManager = Glide.with(mContext);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final Cursor cursor, final int position) {

        tweetOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        if (cursor.moveToPosition(position)) {
            int writerProfileIndex = cursor.getColumnIndex(TwitterSchema.COLUMN_PROFILE_IMAGE_URL);
            int writerNameIndex = cursor.getColumnIndex(TwitterSchema.COLUMN_TWEET_USER_NAME);
            int writerIdIndex = cursor.getColumnIndex(TwitterSchema.COLUMN_TWEET_USER_ID);
            int tweetContentIndex = cursor.getColumnIndex(TwitterSchema.COLUMN_TEXT);
            int createDateAtIndex = cursor.getColumnIndex(TwitterSchema.COLUMN_CREATED_AT);
            int cardviewUrlIndex = cursor.getColumnIndex(TwitterSchema.COLUMN_CARDVIEW_URL);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

            String temp = cursor.getString(createDateAtIndex);
            String cardviewUrl = cursor.getString(cardviewUrlIndex);
            final String profileImageUrl = cursor.getString(writerProfileIndex);

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdAt = null;
            try {
                createdAt = transFormat.parse(temp);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            writerProfile.post(new Runnable() {
                @Override
                public void run() {
                    mGlideRequestManager
                            .load(profileImageUrl)
                            .into(writerProfile);
                }
            });
            metaOgImageUrl = cursor.getString(cardviewUrlIndex);
            writerId.setText(cursor.getString(writerIdIndex));
            writerName.setText(cursor.getString(writerNameIndex));
            tweetContent.setText(cursor.getString(tweetContentIndex));
            createDateIn.setText(dateFormat.format(createdAt));
            createTimeAt.setText(timeFormat.format(createdAt));
            new SetCardViewTask().execute(cardviewUrl);
        }
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
                scrapOgTagImage.setImageResource(R.drawable.twitter_card_default);
                scrapOgTagTitle.setText("Title");
                scrapOgTagUrl.setText(url);
                return;
            }

            // TwitterCard 제목 설정
            Elements metaOgTitle = doc.select("meta[property=og:title]");
            if (metaOgTitle != null && metaOgTitle.attr("content") != "") {
                scrapOgTagTitle.setText(metaOgTitle.attr("content"));
            }
            else {
                scrapOgTagTitle.setText(doc.title());
            }


            // TwitterCard URL 설정
            Elements metaOgUrl = doc.select("meta[property=og:url]");
            if (metaOgUrl != null && metaOgUrl.attr("content") != "") {
                scrapOgTagUrl.setText(metaOgUrl.attr("content"));
            }
            else {
                scrapOgTagUrl.setText(url);
            }


            // TwitterCard Image 설정
            String imageUrl = null;
            Elements metaOgImage = doc.select("meta[property=og:image]");

            if (metaOgImage != null && metaOgImage.attr("content") != "") {
                metaOgImageUrl = metaOgImage.attr("content");
                scrapOgTagImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mGlideRequestManager
                                .load(metaOgImageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .crossFade()
                                .into(scrapOgTagImage);
                    }
                });

            } else {
                scrapOgTagImage.setImageResource(R.drawable.twitter_card_default);
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
