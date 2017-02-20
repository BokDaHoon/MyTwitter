package com.boostcamp.mytwitter.mytwitter.search.adapter.contract;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchDTO;

import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-17.
 */

public interface SearchAdapterContract {
    interface View {
        void notifyAdapter();

        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model<T> {
        void setListData(List<T> data);
    }
}
