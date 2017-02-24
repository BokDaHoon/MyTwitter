package com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnProfileItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnReplyClickListener;

import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-11.
 */

public interface TimelineAdapterContract {
    interface View {
        void notifyAdapter();

        void setOnItemClickListener(OnItemClickListener listener);

        void setOnProfileItemClickListener(OnProfileItemClickListener profileListener);

        void setOnReplyClickListener(OnReplyClickListener replyListener);
    }

    interface Model {
        void setListData(List<Status> listItem);

        void updateItem(Status data);
    }
}
