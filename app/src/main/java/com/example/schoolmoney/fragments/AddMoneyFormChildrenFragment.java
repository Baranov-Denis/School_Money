package com.example.schoolmoney.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Money;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class AddMoneyFormChildrenFragment extends Fragment {

    private AppLab appLab;
    private View view;
    private UUID childUUID;
    private static final String CHILD_UUID = "child_uuid";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_money_form_children, container, false);
        childUUID = (UUID) getArguments().getSerializable(AddMoneyFormChildrenFragment.CHILD_UUID);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Найдите ваш EditText
        EditText editText = view.findViewById(R.id.enter_money_from_child_edit_text);
        // Установите фокус на EditText
        editText.requestFocus();
        // Откройте клавиатуру
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static AddMoneyFormChildrenFragment newInstance(UUID childUUID) {
        Bundle args = new Bundle();
        args.putSerializable(CHILD_UUID, childUUID);
        AddMoneyFormChildrenFragment addMoneyFormChildrenFragment = new AddMoneyFormChildrenFragment();
        addMoneyFormChildrenFragment.setArguments(args);
        return addMoneyFormChildrenFragment;
    }

    private void setButtons() {
        Button saveButton = view.findViewById(R.id.save_button_on_add_money_from_child_fragment);
        Button cancelButton = view.findViewById(R.id.cancel_button_on_add_money_from_child_fragment);
        EditText value = view.findViewById(R.id.enter_money_from_child_edit_text);
        value.setInputType(InputType.TYPE_CLASS_NUMBER);


        // Установите фокус на EditText
        value.requestFocus();

        // Откройте клавиатуру
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(value, InputMethodManager.SHOW_FORCED);

        saveButton.setOnClickListener(o -> {
            // Создаем объект Date, который представляет текущую дату и время.
            Date currentDate = new Date();

            // Установите желаемый локальный язык (например, русский)
            Locale locale = new Locale("ru", "RU");

            // Создаем объект SimpleDateFormat для форматирования даты.
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);

            // Преобразуем текущую дату в строку с заданным форматом.
            String todayDate = dateFormat.format(currentDate);

            UUID moneyUuid = UUID.randomUUID();
            appLab.addNewIncomeMoneyFromChild(moneyUuid, childUUID, appLab.getChildByUUID(childUUID).getChildName(), Integer.parseInt(value.getText().toString()), todayDate);
            AppFragmentManager.openFragment(ChildCardFragment.newInstance(childUUID));
        });
    }


}