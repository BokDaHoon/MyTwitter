package com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;

import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-11.
 */

public interface TimelineAdapterContract {
    interface View {
        void notifyAdapter();

        void setOnItemClickListener(OnItemClickListener listener);
    }

    interface Model {
        void setListData(List<Status> listItem);
    }
}
