package com.boostcamp.mytwitter.mytwitter.scrap.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.base.db.TwitterSchema;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.scrap.adapter.contract.ScrapAdapterContract;
import com.boostcamp.mytwitter.mytwitter.scrap.adapter.holder.ScrapCardviewHolder;
import com.boostcamp.mytwitter.mytwitter.scrap.adapter.holder.ScrapHolder;
import com.boostcamp.mytwitter.mytwitter.scrap.adapter.holder.ScrapImageHolder;
import com.boostcamp.mytwitter.mytwitter.util.Define;

/**
 * Created by DaHoon on 2017-02-21.
 */

public class ScrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
                          implements ScrapAdapterContract.Model, ScrapAdapterContract.View{

    private Activity activity;
    private static Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;
    private int mCount;

    public ScrapAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {

            case Define.TIMELINE_COMMON_TYPE :

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scrap_common, parent, false);
                viewHolder = new ScrapHolder(parent.getContext(), view, mOnItemClickListener);
                break;

            case Define.TIMELINE_IMAGE_TYPE :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scrap_image, parent, false);
                viewHolder = new ScrapImageHolder(parent.getContext(), view, mOnItemClickListener);
                break;
            case Define.TIMELINE_CARDVIEW_TYPE :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scrap_cardview, parent, false);
                viewHolder = new ScrapCardviewHolder(parent.getContext(), view, mOnItemClickListener);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder == null) {
            return;
        }

        if (holder instanceof ScrapHolder) {
            ((ScrapHolder) holder).onBind(mCursor, position);
        } else if (holder instanceof ScrapImageHolder) {
            ((ScrapImageHolder) holder).onBind(mCursor, position);
        } else if (holder instanceof ScrapCardviewHolder) {
            ((ScrapCardviewHolder) holder).onBind(mCursor, position);
        }

    }


    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCursor.moveToPosition(position)) {
            int tweetViewTypeIndex = mCursor.getColumnIndex(TwitterSchema.COLUMN_TWEET_VIEW_TYPE);
            int viewType = mCursor.getInt(tweetViewTypeIndex);

            return viewType;
        } else {
            return -1;
        }
    }

    public void swapCursor(Cursor data){
        mCursor = data;

        if (data != null) {
            mCount = data.getCount();
            this.notifyDataSetChanged();
        } else {
            mCount = 0;
        }
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public long getDataId(int position) {
        if (mCursor.moveToPosition(position)) {
            int tweetIdIndex = mCursor.getColumnIndex(TwitterSchema.COLUMN_TWEET_ID);
            return mCursor.getLong(tweetIdIndex);
        } else{
            return -1;
        }
    }

}
