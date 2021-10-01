package com.leefo.budgetapplication.view;

import com.leefo.budgetapplication.model.ModelObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing all observers who will be updated every time the model is changed.
 */
public class ViewObserverHandler {

    /**
     * Objects that will update when the model is changed.
     */
    private final static List<ViewObserver> observers = new ArrayList<>();

    /**
     * Adds new observer.
     * @param observer Observer to be added.
     */
    public static void addObserver(ViewObserver observer)
    {
        observers.add(observer);
    }

    /**
     * Updates all observers.
     */
    public static void updateObservers()
    {
        for(ViewObserver observer : observers)
        {
            observer.update();
        }
    }

}
