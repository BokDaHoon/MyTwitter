package com.boostcamp.mytwitter.mytwitter.timeline.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.MediaEntity;
import twitter4j.Status;
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

    public TimelineHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);

        mContext = context;
        mOnItemClickListener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final Status status, final int position) {
        User user = status.getUser();
        writerName.setText(user.getName());
        writerId.setText("@" + user.getScreenName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

        Glide.with(mContext)
             .load(user.getProfileImageURL())
             .into(writerProfile);

        if (status.getMediaEntities().length > 0) {
           MediaEntity[] mediaResult = status.getMediaEntities();
           Glide.with(mContext)
                .load(mediaResult[0].getMediaURL())
                .into(tweetImage);
        } else {
           tweetImage.setVisibility(View.GONE);
        }

        createDateIn.setText(dateFormat.format(status.getCreatedAt()));
        createTimeAt.setText(timeFormat.format(status.getCreatedAt()));
        tweetContent.setText(status.getText());
    }

}
