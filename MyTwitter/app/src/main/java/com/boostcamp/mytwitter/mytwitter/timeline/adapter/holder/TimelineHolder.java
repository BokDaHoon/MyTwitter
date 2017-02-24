package com.boostcamp.mytwitter.mytwitter.timeline.adapter.holder;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.MyTwitterApplication;
import com.boostcamp.mytwitter.mytwitter.base.TwitterInfo;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnProfileItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnReplyClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-11.
 */

public class TimelineHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;
    private OnProfileItemClickListener mProfileItemClickListener;
    private OnReplyClickListener mReplyClickListener;

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
    ImageButton tweetReply;
    @BindView(R.id.tweet_favorite)
    ToggleButton tweetFavorite;
    @BindView(R.id.tweet_favorite_count)
    TextView tweetFavoirteCount;

    private RequestManager mGlideRequestManager;
    private String profileImagePath;
    private long tweetId;
    private boolean firstFavoriteFlag; // 처음 상태표시를 위한 좋아요 버튼 체크 여부는 무시하기 위한 Flag
    private final Animation animScale;

    public TimelineHolder(Context context, View itemView, OnItemClickListener listener,
                          OnProfileItemClickListener profileListener, OnReplyClickListener replyListener) {
        super(itemView);

        mContext = context;
        mOnItemClickListener = listener;
        mProfileItemClickListener = profileListener;
        mReplyClickListener = replyListener;
        mGlideRequestManager = Glide.with(mContext);
        firstFavoriteFlag = true;
        animScale = AnimationUtils.loadAnimation(mContext, R.anim.anim_scale_alpha);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final Status status, final int position) {

        tweetId = status.getId();

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

        tweetFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                buttonView.startAnimation(animScale);
            }
        });

        tweetReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplyClickListener != null) {
                    mReplyClickListener.onReplyItemClick(status.getId());
                }
            }
        });

        User user = status.getUser();
        writerName.setText(user.getName());
        writerId.setText("@" + user.getScreenName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

        if (status.isFavorited()) {
            tweetFavorite.setChecked(true);
        } else {
            tweetFavorite.setChecked(false);
            firstFavoriteFlag = false;
        }

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


        createDateIn.setText(dateFormat.format(status.getCreatedAt()));
        createTimeAt.setText(timeFormat.format(status.getCreatedAt()));
        tweetContent.setText(status.getText());
        tweetFavoirteCount.setText(String.valueOf(status.getFavoriteCount()));
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
                count = Integer.valueOf(tweetFavoirteCount.getText().toString()) + 1;
                mTwit.createFavorite(tweetId);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

        private void destroyFavoriteFunc() {
            try {
                count = Integer.valueOf(tweetFavoirteCount.getText().toString()) - 1;
                mTwit.destroyFavorite(tweetId);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(twitter4j.Status status) {
            super.onPostExecute(status);
            MyTwitterApplication.getTwitterApplication().notifyObservers(status);
        }
    }

}
