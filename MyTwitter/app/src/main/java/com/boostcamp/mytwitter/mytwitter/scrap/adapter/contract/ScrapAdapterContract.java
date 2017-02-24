package com.boostcamp.mytwitter.mytwitter.scrap.adapter.contract;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;

import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-22.
 */

public interface ScrapAdapterContract {

    interface View {
        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model {

    }
}
