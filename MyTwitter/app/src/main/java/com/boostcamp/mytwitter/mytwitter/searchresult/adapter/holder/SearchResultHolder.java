package com.boostcamp.mytwitter.mytwitter.searchresult.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchDTO;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-20.
 */

public class SearchResultHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.search_result_profile_image)
    ImageView searchResultProfileImage;
    @BindView(R.id.search_result_name)
    TextView searchResultName;
    @BindView(R.id.search_result_id)
    TextView searchResultId;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private RequestManager mRequestGlideManager;
    private String imageUrl;

    public SearchResultHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        mContext = context;
        mOnItemClickListener = listener;
        mRequestGlideManager = Glide.with(mContext);
        mOnItemClickListener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final User user, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        imageUrl = user.getProfileImageURL();
        searchResultProfileImage.post(new Runnable() {
            @Override
            public void run() {
                mRequestGlideManager
                    .load(imageUrl)
                    .into(searchResultProfileImage);
            }
        });

        searchResultName.setText(user.getName() + "");
        searchResultId.setText("@" + user.getScreenName());
    }
}
