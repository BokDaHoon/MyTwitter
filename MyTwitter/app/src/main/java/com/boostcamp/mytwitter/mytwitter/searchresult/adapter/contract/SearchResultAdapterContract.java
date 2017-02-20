package com.boostcamp.mytwitter.mytwitter.searchresult.adapter.contract;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;

import java.util.List;

import twitter4j.ResponseList;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-17.
 */

public interface SearchResultAdapterContract {

    interface View {
        void notifyAdapter();

        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model {
        void setListData(ResponseList<User> data);
    }
}
