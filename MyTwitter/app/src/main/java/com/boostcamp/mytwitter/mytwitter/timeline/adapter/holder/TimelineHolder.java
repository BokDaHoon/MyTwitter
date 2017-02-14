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

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
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
 * Created by DaHoon on 2017-02-11.
 */

public class TimelineHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

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
    @BindView(R.id.tweet_image)
    ImageView tweetImage;
    @BindView(R.id.tweet_reply)
    ImageView tweetReply;
    @BindView(R.id.tweet_favorite)
    ImageView tweetFavorite;
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

    public TimelineHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);

        mContext = context;
        mOnItemClickListener = listener;
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

        User user = status.getUser();
        writerName.setText(user.getName());
        writerId.setText("@" + user.getScreenName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

        Glide.with(mContext)
             .load(user.getProfileImageURL())
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .into(writerProfile);

        if (status.getMediaEntities().length != 0) { // Tweet에 이미지가 있는 경우
           tweetImage.setVisibility(View.VISIBLE);
           MediaEntity[] mediaResult = status.getMediaEntities();
           Glide.with(mContext)
                .load(mediaResult[0].getMediaURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tweetImage);

            twitterCard.setVisibility(View.GONE);

        } else { // Tweet에 이미지가 없는 경우
           tweetImage.setVisibility(View.GONE);

            URLEntity[] urlResult = status.getURLEntities();
            if(urlResult.length != 0 && urlResult[0].getExpandedURL() != null){
                twitterCard.setVisibility(View.VISIBLE);
                new SetCardViewTask().execute(urlResult[0].getExpandedURL());
            } else {
                twitterCard.setVisibility(View.GONE);
            }

        }

        createDateIn.setText(dateFormat.format(status.getCreatedAt()));
        createTimeAt.setText(timeFormat.format(status.getCreatedAt()));
        tweetContent.setText(status.getText());
        tweetFavoirteCount.setText(String.valueOf(status.getFavoriteCount()));
    }

    class SetCardViewTask extends AsyncTask<String, Void, Document> {

        private String url = "";

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
            if (metaOgTitle != null) {
                ogTagTitle.setText(metaOgTitle.attr("content"));
            }
            else {
                ogTagTitle.setText(doc.title());
            }


            // TwitterCard URL 설정
            Elements metaOgUrl = doc.select("meta[property=og:url]");
            if (metaOgUrl != null) {
                ogTagUrl.setText(metaOgUrl.attr("content"));
            }
            else {
                ogTagUrl.setText(url);
            }


            // TwitterCard Image 설정
            String imageUrl = null;
            Elements metaOgImage = doc.select("meta[property=og:image]");
            if (metaOgImage != null) {
                imageUrl = metaOgImage.attr("content");
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ogTagImage);
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
