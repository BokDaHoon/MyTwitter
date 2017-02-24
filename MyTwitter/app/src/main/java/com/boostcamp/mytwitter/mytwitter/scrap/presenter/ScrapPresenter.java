package com.boostcamp.mytwitter.mytwitter.scrap.presenter;

import com.boostcamp.mytwitter.mytwitter.scrap.adapter.contract.ScrapAdapterContract;

/**
 * Created by DaHoon on 2017-02-22.
 */

public interface ScrapPresenter {
    interface View {
        void displayDialog(int position);
    }

    interface Prsenter {
        void setView(ScrapPresenter.View view);

        void setScrapAdapterView(ScrapAdapterContract.View adapterView);
    }
}
