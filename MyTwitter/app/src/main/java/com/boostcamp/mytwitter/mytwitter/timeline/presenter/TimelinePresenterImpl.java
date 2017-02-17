package com.boostcamp.mytwitter.mytwitter.timeline.presenter;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;
import com.boostcamp.mytwitter.mytwitter.timeline.model.TimelineModel;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class TimelinePresenterImpl implements TimelinePresenter.Presenter, TimelineModel.ModelDataChange, OnItemClickListener {

    private TimelinePresenter.View view;
    private TimelineModel model;

    private TimelineAdapterContract.Model adapterModel;
    private TimelineAdapterContract.View adapterView;

    @Override
    public void setView(TimelinePresenter.View view) {
        this.view = view;
        model = new TimelineModel();
        model.setOnChangeListener(this);
    }

    @Override
    public void initSidebarNavigation() {
        User user = model.getUserInfo();
        view.initSidebarNavigation(user);
    }

    @Override
    public void loadTimelineList() {
        model.callTimelineList();
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
    public void update(List<Status> list) {
        adapterModel.setListData(list);
        adapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(int position) {
        view.moveToDetail(position);
    }
}
