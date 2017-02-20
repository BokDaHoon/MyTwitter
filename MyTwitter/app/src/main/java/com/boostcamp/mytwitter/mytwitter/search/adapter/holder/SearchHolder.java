package com.boostcamp.mytwitter.mytwitter.search.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchDTO;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class SearchHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.search_word_rank)
    TextView searchWordRank;
    @BindView(R.id.search_word)
    TextView searchWord;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public SearchHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        mContext = context;
        mOnItemClickListener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(final SearchDTO search, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        searchWordRank.setText(search.getRank() + "");
        searchWord.setText(search.getSearchWord());
    }

}
