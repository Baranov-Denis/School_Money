package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;

import java.util.UUID;


public class CreateNewParentFragment extends Fragment {
    private AppLab appLab;
    private View view;

    private UUID childUUID;

    private static final String CHILD_UUID = "child_uuid";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_new_parent, container, false);
        childUUID = (UUID) getArguments().getSerializable(CreateNewParentFragment.CHILD_UUID);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    public static CreateNewParentFragment newInstance(UUID childUUID){
        Bundle args = new Bundle();
        args.putSerializable(CHILD_UUID, childUUID);
        CreateNewParentFragment createNewParentFragment = new CreateNewParentFragment();
        createNewParentFragment.setArguments(args);
        return createNewParentFragment;
    }


    private void setButtons() {
        Button saveButton = view.findViewById(R.id.save_button_on_create_parents_fragment);
        Button cancelButton = view.findViewById(R.id.cancel_button_on_create_parents_fragment);
        EditText parentName = view.findViewById(R.id.enter_new_parent_name_edit_text);
        EditText parentPhone = view.findViewById(R.id.enter_new_parent_phone_edit_text);

        saveButton.setOnClickListener(o -> {
            appLab.addNewParent(childUUID, parentName.getText().toString(), parentPhone.getText().toString());
            AppFragmentManager.openFragment(ChildCardFragment.newInstance(childUUID));
        });
    }
}