package com.example.schoolmoney.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Money;
import com.example.schoolmoney.fragments.windows.DeleteMoneyFloatingWindowFragment;

import java.util.UUID;


public class MoneyCardFragment extends Fragment {
    private static final String MONEY_UUID = "money_uuid";
    View view;
    private AppLab appLab;
    private Money money;
    private boolean dontWantToDelete = true;
    private TextView moneyTitle;
    private TextView moneyValue;
    private EditText moneyNote;
    private TextView moneyDate;
    private Button cancelButton;
    private Button deleteButton;
    private ImageButton imageButton;

    private void deleteThisMoney() {
        dontWantToDelete = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_money_card, container, false);
        UUID moneyUUID = (UUID) getArguments().getSerializable(MoneyCardFragment.MONEY_UUID);
        appLab = AppLab.getAppLab(getActivity());
        money = appLab.getMoneyById(moneyUUID);
        bindMoney();
        setButtons();
        updateUI();
        return view;
    }

    public static MoneyCardFragment newInstance(UUID moneyUUID) {
        Bundle args = new Bundle();
        args.putSerializable(MONEY_UUID, moneyUUID);
        MoneyCardFragment moneyCardFragment = new MoneyCardFragment();
        moneyCardFragment.setArguments(args);
        return moneyCardFragment;
    }

    private void updateUI() {

    }

    private void setButtons() {
        cancelButton = view.findViewById(R.id.cancel_button_on_money_card_fragment);
        deleteButton = view.findViewById(R.id.delete_button_on_money_card_fragment);
        imageButton = view.findViewById(R.id.change_money_title_image_button);

        cancelButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new ExpensesPageFragment());
        });

        deleteButton.setOnLongClickListener(o -> {
            deleteThisMoney();
            AppFragmentManager.addFragment(new DeleteMoneyFloatingWindowFragment(money));
            return true;
        });

        imageButton.setOnClickListener(o->{
            moneyTitle.setEnabled(true);
            moneyTitle.requestFocus();
            // Откройте клавиатуру
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(moneyTitle, InputMethodManager.SHOW_IMPLICIT);
        });

    }

    private void bindMoney() {
        moneyTitle = view.findViewById(R.id.money_title_on_money_card_text_view);
        moneyValue = view.findViewById(R.id.money_card_value);
        moneyNote = view.findViewById(R.id.money_card_note);
        moneyDate = view.findViewById(R.id.money_card_date);

        moneyTitle.setText(money.getTitle());
        moneyValue.setText(money.getValueExpenses());
        moneyNote.setText(money.getNote());
        moneyDate.setText(money.getDate());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dontWantToDelete) {
            appLab.changeMoneyTitleAndNote(money, moneyTitle.getText().toString(), moneyNote.getText().toString());
        }
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dontWantToDelete) {
            appLab.changeMoneyTitleAndNote(money, moneyTitle.getText().toString(), moneyNote.getText().toString());
        }
        goToList();
    }

    private void goToList() {
        AppFragmentManager.openFragment(new ExpensesPageFragment());
    }
}