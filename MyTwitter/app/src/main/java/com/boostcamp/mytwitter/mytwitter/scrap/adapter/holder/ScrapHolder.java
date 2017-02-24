package com.boostcamp.mytwitter.mytwitter.scrap.adapter.holder;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.db.TwitterSchema;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class ScrapHolder extends RecyclerView.ViewHolder {

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
    @BindView(R.id.tweet_option_menu)
    ImageButton tweetOptionMenu;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private RequestManager mGlideRequestManager;

    public ScrapHolder(Context context, View itemView, OnItemClickListener listener) {
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

            String temp = cursor.getString(createDateAtIndex);
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
            writerId.setText(cursor.getString(writerIdIndex));
            writerName.setText(cursor.getString(writerNameIndex));
            tweetContent.setText(cursor.getString(tweetContentIndex));
            createDateIn.setText(dateFormat.format(createdAt));
            createTimeAt.setText(timeFormat.format(createdAt));
        }
    }

}
