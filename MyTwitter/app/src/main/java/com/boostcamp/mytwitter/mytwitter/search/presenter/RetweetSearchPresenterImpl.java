package com.boostcamp.mytwitter.mytwitter.search.presenter;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.adapter.contract.RetweetSearchAdapterContract;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchModel;
import com.boostcamp.mytwitter.mytwitter.search.model.TwitterSearchDTO;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-19.
 */

public class RetweetSearchPresenterImpl implements RetweetSearchPresenter.Presenter, SearchModel.RetweetModelDataChange, OnItemClickListener {
    private RetweetSearchPresenter.View view;
    private SearchModel model;
    private RetweetSearchAdapterContract.View adapterView;
    private RetweetSearchAdapterContract.Model adapterModel;

    @Override
    public void setView(RetweetSearchPresenter.View view) {
        this.view = view;
        model = new SearchModel();
        model.setRetweetOnChangeListener(this);
    }

    @Override
    public void loadSearchWordList(int type) {
        model.callLoadSearchWordList(type);
    }

    @Override
    public void setSearchListAdapterModel(RetweetSearchAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setSearchListAdapterView(RetweetSearchAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void update(TwitterSearchDTO list) {
        adapterModel.setListData(list);
        adapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(int position) {
        TwitterSearchDTO.RankedTwitList data = adapterModel.getListData(position);
        Status status = model.getStatusInfo(Long.valueOf(data.getId()));
        view.moveToDetail(status);
    }
}
