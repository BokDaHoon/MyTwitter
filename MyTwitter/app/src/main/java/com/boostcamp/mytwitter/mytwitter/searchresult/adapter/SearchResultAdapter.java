package com.boostcamp.mytwitter.mytwitter.searchresult.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.searchresult.adapter.contract.SearchResultAdapterContract;
import com.boostcamp.mytwitter.mytwitter.searchresult.adapter.holder.SearchResultHolder;

import twitter4j.ResponseList;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-20.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultHolder> implements
                                                    SearchResultAdapterContract.View, SearchResultAdapterContract.Model {

    private Activity activity;
    private ResponseList<User> data;
    private OnItemClickListener mOnItemClickListener;

    public SearchResultAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_result, parent, false);
        SearchResultHolder viewHolder = new SearchResultHolder(parent.getContext(), view, mOnItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int position) {
        holder.onBind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }

        return 0;
    }

    @Override
    public void setListData(ResponseList<User> data) {
        this.data = data;
    }

    public User getListData(int position) {
        return data.get(position);
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
