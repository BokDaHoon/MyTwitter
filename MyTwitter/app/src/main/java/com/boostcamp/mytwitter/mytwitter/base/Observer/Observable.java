package com.boostcamp.mytwitter.mytwitter.base.Observer;

/**
 * Created by DaHoon on 2017-02-20.
 */

public interface Observable {
    public void addObserver(CustomObserver observer);
    public void deleteObserver(CustomObserver observer);
    public void notifyObservers(Object obj);
}
