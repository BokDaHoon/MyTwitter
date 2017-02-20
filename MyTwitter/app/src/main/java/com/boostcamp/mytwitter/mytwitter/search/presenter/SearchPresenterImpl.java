package com.boostcamp.mytwitter.mytwitter.search.presenter;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.search.adapter.contract.SearchAdapterContract;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchDTO;
import com.boostcamp.mytwitter.mytwitter.search.model.SearchModel;

import java.util.List;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class SearchPresenterImpl implements SearchPresenter.Presenter, SearchModel.ModelDataChange<SearchDTO>, OnItemClickListener {

    private SearchPresenter.View view;
    private SearchModel model;
    private SearchAdapterContract.View adapterView;
    private SearchAdapterContract.Model adapterModel;

    @Override
    public void setView(SearchPresenter.View view) {
        this.view = view;
        model = new SearchModel();
        model.setOnChangeListener(this);
    }

    @Override
    public void loadSearchWordList(int type) {
        model.callLoadSearchWordList(type);
    }

    @Override
    public void setSearchListAdapterModel(SearchAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setSearchListAdapterView(SearchAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void update(List<SearchDTO> list) {
        adapterModel.setListData(list);
        adapterView.notifyAdapter();
    }

    @Override
    public void onItemClick(int position) {
        view.moveToSearchResultByWord(position);
    }
}
