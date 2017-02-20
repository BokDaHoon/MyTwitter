package com.boostcamp.mytwitter.mytwitter.search.presenter;

import com.boostcamp.mytwitter.mytwitter.search.adapter.contract.RetweetSearchAdapterContract;

import twitter4j.Status;

/**
 * Created by DaHoon on 2017-02-19.
 */

public interface RetweetSearchPresenter {

    interface View {
        void moveToDetail(Status status);
    }

    interface Presenter {
        void setView(RetweetSearchPresenter.View view);

        void loadSearchWordList(int type);

        void setSearchListAdapterModel(RetweetSearchAdapterContract.Model adapterModel);

        void setSearchListAdapterView(RetweetSearchAdapterContract.View adapterView);

    }

}
