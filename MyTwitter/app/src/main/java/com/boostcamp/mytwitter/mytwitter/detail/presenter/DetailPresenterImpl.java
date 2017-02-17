package com.boostcamp.mytwitter.mytwitter.detail.presenter;

import com.boostcamp.mytwitter.mytwitter.detail.model.DetailModel;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;

import java.util.List;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-15.
 */

public class DetailPresenterImpl implements DetailPresenter.Presenter, DetailModel.ModelDataChange,
                                            OnItemClickListener {

    private TimelineAdapterContract.Model adapterModel;
    private TimelineAdapterContract.View adapterView;

    private DetailPresenter.View view;
    private DetailModel model;


    @Override
    public void setView(DetailPresenter.View view) {
        this.view = view;
        model = new DetailModel();
        model.setOnChangeListener(this);
        model.setPresenter(this);
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
    public void onItemClick(int position) {
        view.moveToDetail(position);
    }

    @Override
    public void update(List<Status> list) {
        if (list != null) {
            adapterModel.setListData(list);
            adapterView.notifyAdapter();
        }
    }
}
