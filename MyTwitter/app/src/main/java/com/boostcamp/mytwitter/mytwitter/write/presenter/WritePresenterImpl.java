package com.boostcamp.mytwitter.mytwitter.write.presenter;

import com.boostcamp.mytwitter.mytwitter.write.model.WriteModel;

import twitter4j.StatusUpdate;

/**
 * Created by DaHoon on 2017-02-13.
 */

public class WritePresenterImpl implements WritePresenter.Presenter {

    private WritePresenter.View view;
    private WriteModel model;

    @Override
    public void setView(WritePresenter.View view) {
        this.view = view;
        model = new WriteModel();
    }

    /**
     * 트윗 버튼 클릭 시 호출.
     * 트윗 작성.
     */
    @Override
    public void writeContent(StatusUpdate content) {
        model.callWriteContent(content);
        view.writeContentSuccess();
        view.viewFinish();
    }

}
