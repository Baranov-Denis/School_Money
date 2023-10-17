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
import com.example.schoolmoney.appLab.Animation;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Money;
import com.example.schoolmoney.appLab.SharedPreferencesHelper;


public class ResultPageFragment extends Fragment {

    private AppLab appLab;
    private View view;
    private TextView totalResult;
    private TextView receiveResult;

    private TextView spentResult;
    private EditText targetMoneyEdit;
    private Button setTargetButton;
    private Button goToSettingsButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result_page, container, false);
        appLab = AppLab.getAppLab(getContext());
        totalResult = view.findViewById(R.id.current_money_result);
        receiveResult = view.findViewById(R.id.all_receive_result);
        spentResult = view.findViewById(R.id.spent_money_result);
        targetMoneyEdit = view.findViewById(R.id.money_target_edit);
        //setTargetButton = view.findViewById(R.id.set_money_target_button);
        goToSettingsButton = view.findViewById(R.id.go_settings_button);
        targetMoneyEdit.setText(SharedPreferencesHelper.getData(getContext()).getMoneyTarget());
        setButtons();
        //AppFragmentManager.createBottomButtons();
        updateUI();
        AppFragmentManager.closeApp(this);
        return view;
    }

    private void updateUI() {
        appLab = AppLab.getAppLab(getActivity());
        totalResult.setText(getTotalResultMoney());
        receiveResult.setText(getTotalReceive());
        spentResult.setText(getSpentMoney());
    }

    private String getTotalResultMoney() {
        int result = 0;
        for (Money money : appLab.getMoneyList()) {
            result += Integer.parseInt(money.getValueIncome());
            result -= Integer.parseInt(money.getValueExpenses());
        }
        return Integer.toString(result);
    }

    private String getSpentMoney() {
        int result = 0;
        for (Money money : appLab.getMoneyList()) {
            result += Integer.parseInt(money.getValueExpenses());
        }
        return Integer.toString(result);
    }

    private String getTotalReceive() {
        int result = 0;
        for (Money money : appLab.getMoneyList()) {
            result += Integer.parseInt(money.getValueIncome());
        }
        return Integer.toString(result);
    }

    private void setButtons() {
     /*   setTargetButton.setOnClickListener(o -> {
            String target = "5";
            target = targetMoneyEdit.getText().toString();
            SharedPreferencesHelper.saveMoneyTarget(getContext(), target);
            targetMoneyEdit.setFocusable(false);
            AppFragmentManager.openFragment(new ChildrenPageFragment(), Animation.FADE_IN);
        });
*/
        goToSettingsButton.setOnClickListener(o -> {
            AppFragmentManager.openFragmentInButtonsView(new SettingsFragment(), Animation.FROM_RIGHT);
        });
    }

    @Override
    public void onPause() {
        String target = "5";
        target = targetMoneyEdit.getText().toString();
        if (!targetMoneyEdit.getText().toString().equals("")) {
            SharedPreferencesHelper.saveMoneyTarget(requireContext(), target);
        }
        super.onPause();

    }
}