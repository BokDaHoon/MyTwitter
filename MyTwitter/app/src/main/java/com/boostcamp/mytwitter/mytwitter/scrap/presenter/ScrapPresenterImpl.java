package com.boostcamp.mytwitter.mytwitter.scrap.presenter;

import com.boostcamp.mytwitter.mytwitter.listener.OnItemClickListener;
import com.boostcamp.mytwitter.mytwitter.scrap.adapter.contract.ScrapAdapterContract;

/**
 * Created by DaHoon on 2017-02-22.
 */

public class ScrapPresenterImpl implements ScrapPresenter.Prsenter, OnItemClickListener {

    private ScrapPresenter.View view;
    private ScrapAdapterContract.View adapterView;

    @Override
    public void setView(ScrapPresenter.View view) {
        this.view = view;
    }

    @Override
    public void setScrapAdapterView(ScrapAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        view.displayDialog(position);
    }
}
