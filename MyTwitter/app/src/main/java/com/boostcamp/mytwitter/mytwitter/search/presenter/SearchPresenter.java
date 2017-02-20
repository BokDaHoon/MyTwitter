package com.boostcamp.mytwitter.mytwitter.search.presenter;

import com.boostcamp.mytwitter.mytwitter.search.adapter.contract.SearchAdapterContract;
import com.boostcamp.mytwitter.mytwitter.timeline.adapter.contract.TimelineAdapterContract;

/**
 * Created by DaHoon on 2017-02-17.
 */

public interface SearchPresenter {

    interface View {
        void moveToSearchResultByWord(int position);
    }

    interface Presenter {
        void setView(SearchPresenter.View view);

        void loadSearchWordList(int type);

        void setSearchListAdapterModel(SearchAdapterContract.Model adapterModel);

        void setSearchListAdapterView(SearchAdapterContract.View adapterView);

    }

}
