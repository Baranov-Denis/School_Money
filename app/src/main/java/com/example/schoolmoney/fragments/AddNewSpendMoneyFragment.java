package com.example.schoolmoney.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddNewSpendMoneyFragment extends Fragment {

    private View view;
    private AppLab appLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_spend_money, container, false);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    private void setButtons() {
        Button saveButton = view.findViewById(R.id.save_button_on_spend_money_fragment);
        Button cancelButton = view.findViewById(R.id.cancel_button_on_spend_money_fragment);
        EditText titleEditText = view.findViewById(R.id.enter_spend_money_title_edit_text);
        EditText valueEditText = view.findViewById(R.id.enter_spend_money_value_edit_text);
        EditText noteEditText = view.findViewById(R.id.enter_spend_money_note_edit_text);

        // Откройте клавиатуру
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(titleEditText, InputMethodManager.SHOW_FORCED);

        saveButton.setOnClickListener(o -> {
            // Создаем объект Date, который представляет текущую дату и время.
            Date currentDate = new Date();

            // Установите желаемый локальный язык (например, русский)
            Locale locale = new Locale("ru", "RU");

            // Создаем объект SimpleDateFormat для форматирования даты.
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);

            // Преобразуем текущую дату в строку с заданным форматом.
            String todayDate = dateFormat.format(currentDate);

            appLab.addNewSpendMoneyFrom(titleEditText.getText().toString(),noteEditText.getText().toString() + "", valueEditText.getText().toString(),todayDate);
            AppFragmentManager.openFragment(new ExpensesPageFragment());
        });
    }


}