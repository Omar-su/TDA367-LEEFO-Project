package com.leefo.budgetapplication.model;


/**
 * Interface for model that is used when it changes.
 * @author Linus Lundgren
 */
public interface ModelObserver {
    /**
     * Method called when model gets changed.
     */
    void update();
}
