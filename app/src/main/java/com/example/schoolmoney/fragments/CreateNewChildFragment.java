package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.Animation;
import com.example.schoolmoney.appLab.AppLab;


public class CreateNewChildFragment extends Fragment {

    private AppLab appLab;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_new_child, container, false);
        appLab = AppLab.getAppLab(getContext());
        setButtons();

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Найдите ваш EditText
        EditText editText = view.findViewById(R.id.enter_new_child_edit_text);
        // Установите фокус на EditText
        editText.requestFocus();
        // Откройте клавиатуру
       // InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
       // imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setButtons() {
        setCancelButton();
        setSaveButton();
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.save_button_on_create_child_fragment);
        EditText childName = view.findViewById(R.id.enter_new_child_edit_text);

        saveButton.setOnClickListener(o -> {
            String childNameString = childName.getText().toString();
            if(!childNameString.equals("")) {
                appLab.addNewChild(childNameString);
                goToList();
            }
        });
    }

    private void setCancelButton() {
        Button cancelButton = view.findViewById(R.id.cancel_button_on_create_child_fragment);
        cancelButton.setOnClickListener(o -> {
            goToList();
        });
    }

    private void goToList() {
        AppFragmentManager.openFragmentInNewButtonsView(new ChildrenListPageFragment(), Animation.FROM_RIGHT,0);
    }



}