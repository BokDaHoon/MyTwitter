package com.boostcamp.mytwitter.mytwitter.detail.presenter;

import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;

import org.jsoup.nodes.Document;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-15.
 */

public interface DetailPresenter {

    interface View {
        void moveToDetail(int position);

        void progressStart();

        void progressStop();

        boolean isFinishingActivity();
    }

    interface Presenter {
        void setView(DetailPresenter.View view);

        void loadReplyList(twitter4j.Status status);

        void setTimelineListAdapterModel(TimelineAdapterContract.Model adapterModel);

        void setTimelineListAdapterView(TimelineAdapterContract.View adapterView);

        void showProgressBar();

        void disappearProgressBar();

        void setFinish();
    }
}
