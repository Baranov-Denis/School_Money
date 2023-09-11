package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExpensesPageFragment extends Fragment {

    private View view;
    private AppLab appLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expenses_page, container, false);
      //  childrenRecycleView = view.findViewById(R.id.child_recycler_view);
      // childrenRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appLab = AppLab.getAppLab(getContext());
        AppFragmentManager.createBottomButtons();
        setFabButton();
       // updateUI();
        return view;
    }

    private void setFabButton() {
        FloatingActionButton addNewChildButton = view.findViewById(R.id.add_new_spend_money_fab_button);
        addNewChildButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new AddNewSpendMoneyFragment());
        });
    }
}