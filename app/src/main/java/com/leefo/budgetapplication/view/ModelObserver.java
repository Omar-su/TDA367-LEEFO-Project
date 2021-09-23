package com.leefo.budgetapplication.view;


/**
 * Interface for model that is used when it changes.
 */
public interface ModelObserver {
    /**
     * Method called when model gets changed.
     */
    void update();
}
