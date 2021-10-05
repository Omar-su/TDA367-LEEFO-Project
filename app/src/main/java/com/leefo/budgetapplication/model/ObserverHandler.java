package com.leefo.budgetapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing all observers who will be updated every time the model is changed.
 *
 * @author Linus Lundgren
 */
public class ObserverHandler {

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
    public static void updateObservers()
    {
        for(ModelObserver observer : observers)
        {
            observer.update();
        }
    }

}
