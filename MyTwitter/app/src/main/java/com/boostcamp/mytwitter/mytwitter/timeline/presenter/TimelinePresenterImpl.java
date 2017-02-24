package com.boostcamp.mytwitter.mytwitter.timeline.presenter;

import com.boostcamp.mytwitter.mytwitter.base.MyTwitterApplication;
import com.boostcamp.mytwitter.mytwitter.base.Observer.CustomObserver;
import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnProfileItemClickListener;
import com.boostcamp.mytwitter.mytwitter.listener.OnReplyClickListener;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;
import com.boostcamp.mytwitter.mytwitter.timeline.model.TimelineModel;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class TimelinePresenterImpl implements TimelinePresenter.Presenter, TimelineModel.ModelDataChange, OnItemClickListener,
                                              OnProfileItemClickListener, OnReplyClickListener, CustomObserver {

    private TimelinePresenter.View view;
    private TimelineModel model;

    private TimelineAdapterContract.Model adapterModel;
    private TimelineAdapterContract.View adapterView;

    // Pager Variables
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int currentPage = 1;


    @Override
    public void setView(TimelinePresenter.View view) {
        this.view = view;
        model = new TimelineModel();
        model.setOnChangeListener(this);
        MyTwitterApplication.getTwitterApplication().addObserver(this);
    }

    @Override
    public void initSidebarNavigation() {
        User user = model.getUserInfo();
        view.initSidebarNavigation(user);
    }

    @Override
    public void loadTimelineList() {
        model.callTimelineList(currentPage);
    }

    @Override
    public void setTimelineListAdapterModel(TimelineAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setTimelineListAdapterView(TimelineAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnItemClickListener(this);
        this.adapterView.setOnProfileItemClickListener(this);
        this.adapterView.setOnReplyClickListener(this);
    }

    @Override
    public void checkListViewPositionBottom(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                currentPage = currentPage + 1;
                loadTimelineList();
            }
        }
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

    @Override
    public void onProfileItemClick(long id) {
        view.moveToProfile(id);
    }

    @Override
    public void update(Object obj) {
        if (obj instanceof Status) {
            adapterModel.updateItem((Status)obj);
        }
        adapterView.notifyAdapter();
    }

    @Override
    public void onReplyItemClick(long statusId) {
        view.moveToReply(statusId);
    }
}
