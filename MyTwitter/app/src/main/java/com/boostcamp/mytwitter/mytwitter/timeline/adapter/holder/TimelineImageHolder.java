package com.boostcamp.mytwitter.mytwitter.timeline.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

public class TimelineImageHolder extends RecyclerView.ViewHolder {

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
    @BindView(R.id.tweet_image)
    ImageView tweetImage;
    @BindView(R.id.tweet_reply)
    ImageView tweetReply;
    @BindView(R.id.tweet_favorite)
    ToggleButton tweetFavorite;
    @BindView(R.id.tweet_favorite_count)
    TextView tweetFavoirteCount;

    private RequestManager mGlideRequestManager;
    private String profileImagePath;
    private MediaEntity[] mediaResult;

    public TimelineImageHolder(Context context, View itemView, OnItemClickListener listener, OnProfileItemClickListener profileListener) {
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

        // 트윗 작성자 프로필 사진 세팅.
        writerProfile.post(new Runnable() {
            @Override
            public void run() {
                mGlideRequestManager
                        .load(profileImagePath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(writerProfile);
            }
        });

        // 트윗 이미지 세팅.
        mediaResult = status.getMediaEntities();
        tweetImage.post(new Runnable() {
            @Override
            public void run() {
                mGlideRequestManager
                .load(mediaResult[0].getMediaURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tweetImage);
            }
        });


        if (status.isFavorited()) {
            tweetFavorite.setChecked(true);
        } else {
            tweetFavorite.setChecked(false);
        }

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
}
