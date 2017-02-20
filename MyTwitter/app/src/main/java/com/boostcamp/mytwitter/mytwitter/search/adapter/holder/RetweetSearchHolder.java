package com.boostcamp.mytwitter.mytwitter.search.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.model.TwitterSearchDTO;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.Twitter;

/**
 * Created by DaHoon on 2017-02-19.
 */

public class RetweetSearchHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.retweet_word_rank)
    TextView retweetWordRank;
    @BindView(R.id.retweet_search_profile)
    ImageView retweetSearchImage;
    @BindView(R.id.retweet_search_id)
    TextView retweetSearchId;
    @BindView(R.id.retweet_search_content)
    TextView retweetSearchContent;
    @BindView(R.id.retweet_search_retweet_count)
    TextView retweetSearchRetweetCount;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public RetweetSearchHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        mContext = context;
        mOnItemClickListener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final TwitterSearchDTO.RankedTwitList search, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        retweetWordRank.setText(search.getRtRank());

        Glide.with(mContext)
             .load(search.getOwnerProfileImgUrl())
             .into(retweetSearchImage);

        retweetSearchId.setText(search.getOwner());
        retweetSearchContent.setText(search.getBody());
        retweetSearchRetweetCount.setText(search.getRtCount());
    }
}
