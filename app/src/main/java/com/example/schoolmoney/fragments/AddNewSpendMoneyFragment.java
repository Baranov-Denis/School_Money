package com.example.schoolmoney.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddNewSpendMoneyFragment extends Fragment {
    String stringDate = null;
    private View view;
    private AppLab appLab;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView addMoneyDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_spend_money, container, false);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        setDatePickerDialog();
        return view;
    }

    private void setButtons() {
        Button saveButton = view.findViewById(R.id.save_button_on_spend_money_fragment);
        Button cancelButton = view.findViewById(R.id.cancel_button_on_spend_money_fragment);
        EditText titleEditText = view.findViewById(R.id.enter_spend_money_title_edit_text);
        EditText valueEditText = view.findViewById(R.id.enter_spend_money_value_edit_text);
        EditText noteEditText = view.findViewById(R.id.enter_spend_money_note_edit_text);
        addMoneyDate = view.findViewById(R.id.add_money_date);
        addMoneyDate.setText(getTodayDate());

        // Откройте клавиатуру
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(titleEditText, InputMethodManager.SHOW_FORCED);

        saveButton.setOnClickListener(o -> {
            String todayDate = null;
            if(stringDate == null) {

                todayDate = getTodayDate();

            }else{
                todayDate = stringDate;
            }
            if(!titleEditText.getText().toString().equals("")&&!valueEditText.getText().toString().equals("")) {
                appLab.addNewSpendMoneyFrom(titleEditText.getText().toString(), noteEditText.getText().toString() + "", valueEditText.getText().toString(), todayDate);
                AppFragmentManager.openFragment(new ExpensesPageFragment());
            }
        });

        cancelButton.setOnClickListener(o->{
            AppFragmentManager.openFragment(new ExpensesPageFragment());
        });
    }

    private String getTodayDate(){
        // Создаем объект Date, который представляет текущую дату и время.
        Date currentDate = new Date();

        // Установите желаемый локальный язык (например, русский)
        Locale locale = new Locale("ru", "RU");

        // Создаем объект SimpleDateFormat для форматирования даты.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);

        // Преобразуем текущую дату в строку с заданным форматом.
        return dateFormat.format(currentDate);
    }

    private void setDatePickerDialog(){
        Button setDateButton = view.findViewById(R.id.set_date_button);

        setDateButton.setOnClickListener(o->{
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, year,month,day);
            datePickerDialog.show();
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                stringDate = dayOfMonth + "/" + month + "/" + year;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
                Date date = null;
                try {
                    date = sdf.parse(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Установите желаемый локальный язык (например, русский)
                Locale locale = new Locale("ru", "RU");
                // Создаем объект SimpleDateFormat для форматирования даты.
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
                stringDate = dateFormat.format(date);
                addMoneyDate.setText(stringDate);
            }
        };
    }

}