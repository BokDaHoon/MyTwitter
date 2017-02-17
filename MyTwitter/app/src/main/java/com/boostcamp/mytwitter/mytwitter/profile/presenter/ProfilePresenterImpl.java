package com.boostcamp.mytwitter.mytwitter.profile.presenter;

import android.util.Log;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.profile.model.ProfileModel;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-16.
 */

public class ProfilePresenterImpl implements ProfilePresenter.Presenter, ProfileModel.ModelDataChange
                                           , OnItemClickListener {

    private TimelineAdapterContract.Model adapterModel;
    private TimelineAdapterContract.View adapterView;

    ProfilePresenter.View view;
    ProfileModel model;

    @Override
    public void setView(ProfilePresenter.View view) {
        this.view = view;
        model = new ProfileModel();
        model.setOnChangeListener(this);
    }

    @Override
    public void initMyProfile() {
        User user = model.getUserInfo();
        view.setMyProfile(user);
    }

    @Override
    public void setTimelineListAdapterModel(TimelineAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setTimelineListAdapterView(TimelineAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void loadUserTweetList(long id) {
        model.callLoadUserTweetList(id);
    }

    @Override
    public void update(List<Status> list) {
        adapterModel.setListData(list);
        adapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(int position) {
        view.moveToDetail(position);
    }

}
