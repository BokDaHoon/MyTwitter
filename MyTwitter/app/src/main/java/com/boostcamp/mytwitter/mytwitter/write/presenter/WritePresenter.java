package com.boostcamp.mytwitter.mytwitter.write.presenter;

import twitter4j.StatusUpdate;

/**
 * Created by DaHoon on 2017-02-13.
 */

public interface WritePresenter {

    interface View {
        void writeContentSuccess();
        void viewFinish();
    }

    interface Presenter {
        void setView(WritePresenter.View view);
        void writeContent(StatusUpdate content);
    }
}
