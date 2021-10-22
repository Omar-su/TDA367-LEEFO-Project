package com.leefo.budgetapplication.view.fragments;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.leefo.budgetapplication.R;
import com.leefo.budgetapplication.controller.TransactionController;
import com.leefo.budgetapplication.model.FilterOption;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.SearchSortFilterTransactions;
import com.leefo.budgetapplication.model.SortOption;
import com.leefo.budgetapplication.view.data.ParcelableTransaction;
import com.leefo.budgetapplication.view.data.TimePeriodViewModel;
import com.leefo.budgetapplication.view.data.TimePeriod;
import com.leefo.budgetapplication.view.adapters.TransactionListAdapter;

import java.util.List;

/**
 * This class represents the fragment for the list view inside the HomeFragment.
 * The fragment shows a list with either all transactions or transactions for a chosen month.
 * The search field enables the user to search for a specific transaction by amount and note description.
 * The sort button enables the user to sort the transactions by:
 * - Newest date
 * - Oldest date
 * - Largest amount
 * - Smallest amount
 * The filter button enables the user to filter transaction by:
 * - All categories
 * - Expense
 * - Income
 * The class uses SearchSortFilterTransactions class.
 * Opens Edit Transaction, when a transaction is clicked.
 * Opened from HomeFragment.
 *
 * @author Emelie Edberg, Eugene Dvoryankov
 */
public class HomeListViewFragment extends Fragment {

    private ListView listView;
    private TextView noTransactions1, noTransactions2;
    private TimePeriod timePeriod;
    private ImageButton sort_button;
    private Dialog sortDialog;
    private EditText search_text;
    private ImageButton filter_button;
    private Dialog filterDialog;
    private boolean filterIsActivated = false;
    private List<FinancialTransaction> currentTimePeriodTransactionList;
    private SearchSortFilterTransactions ssf;

    /**
     * Method that runs when the fragment is being created.
     * Connects the fragment xml file to the fragment class and initializes the fragment's components.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list_view, container, false);

        // get views
        listView = view.findViewById(R.id.listView_home);
        noTransactions1 = view.findViewById(R.id.noTransactionsYetText1);
        noTransactions2 = view.findViewById(R.id.noTransactionsYetText2);
        sort_button = view.findViewById(R.id.sort_button);
        search_text = view.findViewById(R.id.search_text);
        filter_button = view.findViewById(R.id.filter_button);

        // get timePeriod from viewModel
        TimePeriodViewModel viewModel = new ViewModelProvider(requireActivity()).get(TimePeriodViewModel.class);
        timePeriod = viewModel.getTimePeriodLiveData().getValue();

        // get Transaction list
        currentTimePeriodTransactionList = TransactionController.getInstance().getTransactions(timePeriod.getMonth(), timePeriod.getYear());

        // create sort search filter object and send in the current list.
        ssf = new SearchSortFilterTransactions(currentTimePeriodTransactionList);

        // update and init
        updateList();

        initTimePeriodOnChangedListener(viewModel);
        initListOnClickListener();

        initSortDialog();
        initSortButton();
        initFilterDialog();
        initFilterButton();
        initSearch();

        return view;
    }

    private void initTimePeriodOnChangedListener(TimePeriodViewModel viewModel) {
        viewModel.getTimePeriodLiveData().observe(getViewLifecycleOwner(), new Observer<TimePeriod>() {
            @Override
            public void onChanged(TimePeriod newTimePeriod) {
                currentTimePeriodTransactionList = TransactionController.getInstance().getTransactions(timePeriod.getMonth(), timePeriod.getYear());
                ssf.updateSourceData(currentTimePeriodTransactionList);
                updateList();
            }
        });
    }

    private void initSearch() {
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ssf.setSearchString(editable.toString());
                updateList();
            }
        });
    }


    private void initListOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FinancialTransaction transaction = (FinancialTransaction) adapterView.getItemAtPosition(i);
                if (transaction.getCategory().getName().equals("DATE")) { // then it is a date row, should not be clickable
                    return;
                }

                Fragment fragment = new EditTransactionFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CHOSEN_TRANSACTION", new ParcelableTransaction(transaction));
                fragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_main, fragment).commit();
            }
        });
    }

    private void updateLabelsAndButtons(List<FinancialTransaction> list) {

        if (list.isEmpty()) {

            if (!search_text.getText().toString().equals("")) {
                noTransactions1.setText("No transactions matches your search.");
                noTransactions2.setText("");
            } else if (filterIsActivated) {
                noTransactions1.setText("No transactions matches your filter option.");
                noTransactions2.setText("");
            } else {
                noTransactions1.setText("You haven't added any transactions for this time period.");
                noTransactions2.setText("Use the plus button to add a new transaction.");

                search_text.setVisibility(View.INVISIBLE);
                sort_button.setVisibility(View.INVISIBLE);
                filter_button.setVisibility(View.INVISIBLE);
            }
            noTransactions1.setVisibility(View.VISIBLE);
            noTransactions2.setVisibility(View.VISIBLE);
        } else {
            noTransactions1.setVisibility(View.INVISIBLE);
            noTransactions2.setVisibility(View.INVISIBLE);

            sort_button.setVisibility(View.VISIBLE);
            filter_button.setVisibility(View.VISIBLE);
            search_text.setVisibility(View.VISIBLE);

        }
    }

    private void updateList() {

        List<FinancialTransaction> list = ssf.getResult();

        updateLabelsAndButtons(list);

        TransactionListAdapter adapter = new TransactionListAdapter(requireActivity().getApplicationContext(), list);
        listView.setAdapter(adapter);
    }

    private void initSortButton() {
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog.show();
            }
        });
    }

    private void initSortDialog() {
        sortDialog = new Dialog(getActivity());
        sortDialog.setContentView(R.layout.sort_dialog);
        RadioGroup sort_radio_group = sortDialog.findViewById(R.id.sort_radio_group);
        sort_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.newest_date_radio:
                        setDeActivatedColor(sort_button);
                        ssf.sortBy(SortOption.NEWEST_DATE);
                        break;

                    case R.id.oldest_date_radio:
                        ssf.sortBy(SortOption.OLDEST_DATE);
                        setActivatedColor(sort_button);
                        break;

                    case R.id.largest_amount_radio:
                        ssf.sortBy(SortOption.LARGEST_AMOUNT);
                        setActivatedColor(sort_button);
                        break;

                    case R.id.smallest_amount_radio:
                        ssf.sortBy(SortOption.SMALLEST_AMOUNT);
                        setActivatedColor(sort_button);
                        break;
                }
                updateList();
                sortDialog.cancel();
            }
        });
    }

    private void initFilterButton() {
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.show();
            }
        });
    }

    private void initFilterDialog() {
        filterDialog = new Dialog(getActivity());
        filterDialog.setContentView(R.layout.filter_dialog);
        RadioGroup filter_radio_group = filterDialog.findViewById(R.id.filter_radio_group);
        filter_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.all_categories_radio:
                        filterIsActivated = false;
                        setDeActivatedColor(filter_button);
                        ssf.setFilter(FilterOption.ALL_CATEGORIES);

                        break;
                    case R.id.expenses_radio:
                        filterIsActivated = true;
                        setActivatedColor(filter_button);
                        ssf.setFilter(FilterOption.EXPENSE);
                        break;

                    case R.id.income_radio:
                        filterIsActivated = true;
                        setActivatedColor(filter_button);
                        ssf.setFilter(FilterOption.INCOME);
                        break;
                }
                updateList();
                filterDialog.cancel();
            }
        });
    }

    private void setActivatedColor(ImageButton button) {
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.teal_200)));
    }

    private void setDeActivatedColor(ImageButton button) {
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffd6d7d7")));
    }
}