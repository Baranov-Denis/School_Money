package com.example.schoolmoney.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.schoolmoney.appLab.Animation;
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
        assert getArguments() != null;
        UUID moneyUUID = (UUID) getArguments().getSerializable(MoneyCardFragment.MONEY_UUID);
        appLab = AppLab.getAppLab(getActivity());
        money = appLab.getMoneyById(moneyUUID);
        bindMoney();
        setButtons();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Получите активность, в которой находится фрагмент
        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        // Включите обработку кнопки "Назад"
        activity.getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Здесь вы можете определить, что должно произойти при нажатии кнопки "Назад"
                // Например, выполнить какое-то действие или перейти на другой фрагмент
                // Если вы хотите, чтобы кнопка "Назад" просто закрыла фрагмент, можно вызвать
                // метод requireActivity().onBackPressed().
                goToList();
            }
        });
    }

    public static MoneyCardFragment newInstance(UUID moneyUUID) {
        Bundle args = new Bundle();
        args.putSerializable(MONEY_UUID, moneyUUID);
        MoneyCardFragment moneyCardFragment = new MoneyCardFragment();
        moneyCardFragment.setArguments(args);
        return moneyCardFragment;
    }



    private void setButtons() {
        cancelButton = view.findViewById(R.id.cancel_button_on_money_card_fragment);
        deleteButton = view.findViewById(R.id.delete_button_on_money_card_fragment);
        imageButton = view.findViewById(R.id.change_money_title_image_button);

        cancelButton.setOnClickListener(o -> {
            goToList();
        });

        deleteButton.setOnLongClickListener(o -> {
            deleteThisMoney();
            AppFragmentManager.addFragment(new DeleteMoneyFloatingWindowFragment(money),Animation.FADE_IN);
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

        int inc = Integer.parseInt(money.getValueExpenses());
        int out = Integer.parseInt(money.getValueIncome());
        int allInt = inc+out;
        String all = Integer.toString(allInt);

        moneyTitle.setText(money.getTitle());
        moneyValue.setText(all);
        moneyNote.setText(money.getNote());
        moneyDate.setText(money.getDate());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dontWantToDelete) {
            appLab.changeMoneyTitleAndNote(money, moneyTitle.getText().toString(), moneyNote.getText().toString());
        }

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
        AppFragmentManager.openFragmentInNewButtonsView(new SpendsListPageFragment(), Animation.FROM_LEFT,1);
    }
}