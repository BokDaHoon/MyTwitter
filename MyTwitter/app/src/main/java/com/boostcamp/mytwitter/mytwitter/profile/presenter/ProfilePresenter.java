package com.boostcamp.mytwitter.mytwitter.profile.presenter;

import com.boostcamp.mytwitter.mytwitter.timeline.adapter.TimelineAdapter;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;

import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-16.
 */

public interface ProfilePresenter {

    interface View {
        void setMyProfile(User user);

        void moveToDetail(int position);
    }

    interface Presenter {
        void setView(ProfilePresenter.View view);

        void initMyProfile();

        void initOtherProfile(long id);

        void setTimelineListAdapterModel(TimelineAdapterContract.Model adapterModel);

        void setTimelineListAdapterView(TimelineAdapterContract.View adapterView);

        void loadUserTweetList(long id);
    }

}
