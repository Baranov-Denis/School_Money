package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class AddMoneyFormChildrenFragment extends Fragment {

    private AppLab appLab;
    private View view;
    private UUID childUUID;
    private static final String CHILD_UUID = "child_uuid";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_money_form_children,container,false);
        childUUID = (UUID) getArguments().getSerializable(AddMoneyFormChildrenFragment.CHILD_UUID);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    public static AddMoneyFormChildrenFragment newInstance(UUID childUUID){
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


        saveButton.setOnClickListener(o -> {
            // Создаем объект Date, который представляет текущую дату и время.
            Date currentDate = new Date();

            // Создаем объект SimpleDateFormat для форматирования даты.
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Преобразуем текущую дату в строку с заданным форматом.
            String todayDate = dateFormat.format(currentDate);

            appLab.addNewIncomeMoney(childUUID,appLab.getChildByUUID(childUUID).getChildName(),Integer.parseInt(value.getText().toString()),todayDate);
            AppFragmentManager.openFragment(ChildCardFragment.newInstance(childUUID));
        });
    }


}