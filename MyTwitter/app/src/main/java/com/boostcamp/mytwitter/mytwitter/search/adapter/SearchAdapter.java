package com.boostcamp.mytwitter.mytwitter.search.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.mytwitter.mytwitter.R;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.adapter.contract.SearchAdapterContract;
import com.boostcamp.mytwitter.mytwitter.search.adapter.holder.SearchHolder;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder>
                           implements SearchAdapterContract.View, SearchAdapterContract.Model<SearchDTO> {

    private Activity activity;
    private List<SearchDTO> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public SearchAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_word, parent, false);
        SearchHolder viewHolder = new SearchHolder(parent.getContext(), view, mOnItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        holder.onBind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
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
    public void setListData(List<SearchDTO> data) {
        mList = data;
    }

    public SearchDTO getListData(int position) {
        return mList.get(position);
    }

}
