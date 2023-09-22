package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Money;
import com.example.schoolmoney.appLab.Settings;


public class ResultPageFragment extends Fragment {

private AppLab appLab;
private View view;
private TextView totalResult;
private TextView receiveResult;
private EditText targetMoneyEdit;
private Button setTargetButton;
private Button goToSettingsButton;
private Settings settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result_page, container, false);
        appLab = AppLab.getAppLab(getContext());
        totalResult = view.findViewById(R.id.test_result);
        receiveResult = view.findViewById(R.id.all_receive_result);
        targetMoneyEdit = view.findViewById(R.id.money_target_edit);
        setTargetButton = view.findViewById(R.id.set_money_target_button);
        goToSettingsButton = view.findViewById(R.id.go_settings_button);
        settings = appLab.getSettings();
        targetMoneyEdit.setText(settings.getMoneyTarget());
        setButtons();
        AppFragmentManager.createBottomButtons();
        updateUI();
        AppFragmentManager.closeApp(this);
        return view;
    }

    private void updateUI() {
        appLab = AppLab.getAppLab(getActivity());
        totalResult.setText(getTotalResultMoney());
        receiveResult.setText(getTotalReceive());

    }

    private String getTotalResultMoney(){
        int result = 0;
        for(Money money : appLab.getMoneyList()){
            result += Integer.parseInt(money.getValueIncome());
            result -= Integer.parseInt(money.getValueExpenses());
        }
        return Integer.toString(result);
    }

    private String getTotalReceive(){
        int result = 0;
        for(Money money : appLab.getMoneyList()){
            result += Integer.parseInt(money.getValueIncome());
        }
        return Integer.toString(result);
    }

    private void setButtons(){
        setTargetButton.setOnClickListener(o->{
            String target = "5";
            target = targetMoneyEdit.getText().toString();
            appLab.addMoneyTarget(target);
            targetMoneyEdit.setFocusable(false);
            AppFragmentManager.openFragment(new ChildrenPageFragment());
        });

        goToSettingsButton.setOnClickListener(o->{
            AppFragmentManager.openFragment(new SettingsFragment());
        });
    }
}