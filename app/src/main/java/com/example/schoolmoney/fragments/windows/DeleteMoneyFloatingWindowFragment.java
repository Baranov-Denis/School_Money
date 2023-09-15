package com.example.schoolmoney.fragments.windows;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Child;
import com.example.schoolmoney.appLab.Money;
import com.example.schoolmoney.fragments.AppFragmentManager;
import com.example.schoolmoney.fragments.ChildrenPageFragment;
import com.example.schoolmoney.fragments.ExpensesPageFragment;

import java.util.UUID;


public class DeleteMoneyFloatingWindowFragment extends Fragment {
    private View view;
    private AppLab appLab;
    private Money money;

    public DeleteMoneyFloatingWindowFragment(Money money) {
        this.money = money;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delete_money_floating_window, container, false);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    private void setButtons() {
        Button cancelButton = view.findViewById(R.id.chancel_delete_money_floating_window_button);
        Button deleteButton = view.findViewById(R.id.delete_money_floating_window_button);

        cancelButton.setOnClickListener(o -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        deleteButton.setOnLongClickListener(ol -> {
            appLab.deleteMoneyByMoneyId(money.getMoneyUuid());
            AppFragmentManager.openFragment(new ExpensesPageFragment());
            return true;
        });
    }
}