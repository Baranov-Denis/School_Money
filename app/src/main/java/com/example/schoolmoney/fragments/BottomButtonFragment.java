package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schoolmoney.R;


public class BottomButtonFragment extends Fragment {

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_buttons, container, false);
        setButtons();
        return view;
    }

    private void setButtons() {
        createReceiptsButton();
        createExpensesButton();
        createResultButton();
    }

    private void createReceiptsButton() {
        Button receiptsButton = view.findViewById(R.id.go_to_receipts_page_button);
        receiptsButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new ChildrenPageFragment());
        });
    }

    private void createExpensesButton() {
        Button receiptsButton = view.findViewById(R.id.go_to_expenses_page_button);
        receiptsButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new ExpensesPageFragment());
        });
    }

    private void createResultButton() {
        Button receiptsButton = view.findViewById(R.id.go_to_result_page_button);
        receiptsButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new ResultPageFragment());
        });
    }




}