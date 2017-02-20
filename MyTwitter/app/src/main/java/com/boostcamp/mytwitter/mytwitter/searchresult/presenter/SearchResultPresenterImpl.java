package com.boostcamp.mytwitter.mytwitter.searchresult.presenter;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.searchresult.adapter.contract.SearchResultAdapterContract;
import com.boostcamp.mytwitter.mytwitter.searchresult.model.SearchResultModel;

import java.util.concurrent.ExecutionException;

import twitter4j.ResponseList;
import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-20.
 */

public class SearchResultPresenterImpl implements SearchResultPresenter.Presenter, SearchResultModel.ModelDataChange, OnItemClickListener {

    SearchResultPresenter.View view;
    SearchResultModel model;
    SearchResultAdapterContract.View adapterView;
    SearchResultAdapterContract.Model adapterModel;

    @Override
    public void setView(SearchResultPresenter.View view) {
        this.view = view;
        model = new SearchResultModel();
        model.setOnChangeListener(this);
    }

    @Override
    public void loadSearchResult(String name) throws ExecutionException, InterruptedException {
        model.callLoadSearchResult(name);
    }

    @Override
    public void setSearchListAdapterView(SearchResultAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void setSearchListAdapterModel(SearchResultAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void update(ResponseList<User> list) {
        adapterModel.setListData(list);
        adapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(int position) {
        view.moveToProfile(position);
    }
}
