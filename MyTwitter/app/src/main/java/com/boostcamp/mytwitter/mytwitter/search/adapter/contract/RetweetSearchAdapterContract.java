package com.boostcamp.mytwitter.mytwitter.search.adapter.contract;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.model.TwitterSearchDTO;

import java.util.List;

/**
 * Created by DaHoon on 2017-02-18.
 */

public interface RetweetSearchAdapterContract {
    interface View {
        void notifyAdapter();

        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model<T> {
        TwitterSearchDTO.RankedTwitList getListData(int position);
        void setListData(TwitterSearchDTO data);
    }
}
