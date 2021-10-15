package com.leefo.budgetapplication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.view.ParcelableCategory;
import com.leefo.budgetapplication.view.adapters.ManageCategoriesListAdapter;

import java.util.ArrayList;

/**
 * @author Emelie Edberg, Eugene Dvoryankov
 */
public class StreakFragment extends Fragment {

    private Button newCategoryButton;
    private ListView listView;
    private RadioGroup radioGroup;
    private ManageCategoriesListAdapter adapter;
    private ImageButton cross;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);



        return view;
    }





}