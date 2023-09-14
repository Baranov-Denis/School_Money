package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Child;
import com.example.schoolmoney.appLab.Money;

import java.util.List;


public class ResultPageFragment extends Fragment {

private AppLab appLab;
private View view;
private TextView test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result_page, container, false);
        appLab = AppLab.getAppLab(getContext());
        test = view.findViewById(R.id.test_result);
        AppFragmentManager.createBottomButtons();
        updateUI();
        AppFragmentManager.closeApp(this);
        return view;
    }

    private void updateUI() {
        appLab = AppLab.getAppLab(getActivity());
        List<Money> moneyList = appLab.getMoneyList();
        test.setText(getTotalResultMoney());

    }

    private String getTotalResultMoney(){
        int result = 0;
        for(Money money : appLab.getMoneyList()){
            result += Integer.parseInt(money.getValueIncome());
            result -= Integer.parseInt(money.getValueExpenses());
        }
        return Integer.toString(result);
    }
}