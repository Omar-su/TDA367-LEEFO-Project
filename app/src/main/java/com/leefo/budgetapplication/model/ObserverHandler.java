package com.leefo.budgetapplication.model;

import com.leefo.budgetapplication.view.ModelObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing all observers who will be updated every time the model is changed.
 */
public abstract class ObserverHandler {

    /**
     * Objects that will update when the model is changed.
     */
    private final static List<ModelObserver> observers = new ArrayList<>();

    /**
     * Adds new observer.
     * @param observer Observer to be added.
     */
    public static void addObserver(ModelObserver observer)
    {
        observers.add(observer);
    }

    /**
     * Updates all observers.
     */
    protected static void updateObservers()
    {
        for(ModelObserver observer : observers)
        {
            observer.update();
        }
    }

}
