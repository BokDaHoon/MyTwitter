package com.boostcamp.mytwitter.mytwitter.timeline.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.holder.TimelineHolder;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-11.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineHolder>
                             implements TimelineAdapterContract.View, TimelineAdapterContract.Model {

    private OnItemClickListener mOnItemClickListener;
    private Activity mActivity;

    private List<Status> mList = new ArrayList<>();

    public TimelineAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timeline, parent, false);
        TimelineHolder viewHolder = new TimelineHolder(parent.getContext(), view, mOnItemClickListener);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(TimelineHolder holder, int position) {
        if (holder == null) {
            return;
        }

        holder.onBind(mList.get(position), position);

    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }

        return mList.size();

    }


    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();

    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;

    }

    @Override
    public void setListData(List<Status> listItem) {
        mList = listItem;

    }
}
