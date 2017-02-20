package com.boostcamp.mytwitter.mytwitter.searchresult.presenter;

import com.boostcamp.mytwitter.mytwitter.search.adapter.contract.RetweetSearchAdapterContract;
import com.boostcamp.mytwitter.mytwitter.searchresult.adapter.contract.SearchResultAdapterContract;

import java.util.concurrent.ExecutionException;

import twitter4j.User;

/**
 * Created by DaHoon on 2017-02-20.
 */

public interface SearchResultPresenter {

    interface View {
        void moveToProfile(int position);
    }

    interface Presenter {
        void setView(SearchResultPresenter.View view);

        void loadSearchResult(String name) throws ExecutionException, InterruptedException;

        void setSearchListAdapterView(SearchResultAdapterContract.View adapterView);

        void setSearchListAdapterModel(SearchResultAdapterContract.Model adapterModel);

    }
}
