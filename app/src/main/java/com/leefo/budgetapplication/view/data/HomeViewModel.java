package com.leefo.budgetapplication.view.data;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for HomeFragment.
 * HomeFragment need one boolean to survive after the fragment is destroyed, it is stored here.
 *
 * used only by HomeFragment.
 *
 * @author Emelie Edberg
 */
public class HomeViewModel extends ViewModel {

    public boolean lastOpenedViewWasCategoryView = true;

}
