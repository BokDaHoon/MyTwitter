package com.boostcamp.mytwitter.mytwitter.detail.presenter;

import com.boostcamp.mytwitter.mytwitter.base.MyTwitterApplication;
import com.boostcamp.mytwitter.mytwitter.base.Observer.CustomObserver;
import com.boostcamp.mytwitter.mytwitter.base.Observer.Observable;
import com.boostcamp.mytwitter.mytwitter.detail.model.DetailModel;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;

import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-15.
 */

public class DetailPresenterImpl implements DetailPresenter.Presenter, DetailModel.ModelDataChange,
                                            OnItemClickListener, CustomObserver {

    private TimelineAdapterContract.Model adapterModel;
    private TimelineAdapterContract.View adapterView;

    private DetailPresenter.View view;
    private DetailModel model;

    private boolean isFinished = false;

    @Override
    public void setView(DetailPresenter.View view) {
        this.view = view;
        model = new DetailModel();
        model.setOnChangeListener(this);
        model.setPresenter(this);
        MyTwitterApplication.getTwitterApplication().addObserver(this);

    }

    @Override
    public void loadReplyList(Status status) {
        model.callLoadReplyList(status);
    }

    @Override
    public void setTimelineListAdapterModel(TimelineAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setTimelineListAdapterView(TimelineAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        adapterView.setOnItemClickListener(this);
    }

    @Override
    public void showProgressBar() {
        view.progressStart();
    }

    @Override
    public void disappearProgressBar() {
        view.progressStop();
    }

    @Override
    public void setFinish() {
        isFinished = true;
        model.finishedTask();
    }

    @Override
    public void onItemClick(int position) {
        view.moveToDetail(position);
    }

    @Override
    public void update(List<Status> list) {
        if (list != null && view.isFinishingActivity() == false) {
            adapterModel.setListData(list);
            adapterView.notifyAdapter();
        }
    }


    @Override
    public void update(Object obj) {

    }

    public boolean checkFinished() {
        return isFinished;
    }
}
