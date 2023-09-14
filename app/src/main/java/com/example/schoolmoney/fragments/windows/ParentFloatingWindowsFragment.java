package com.example.schoolmoney.fragments.windows;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Child;
import com.example.schoolmoney.fragments.AppFragmentManager;
import com.example.schoolmoney.fragments.ChildCardFragment;

public class ParentFloatingWindowsFragment extends Fragment {

    private View view;
    private AppLab appLab;

    private Child child;
    private String parentName;

    public ParentFloatingWindowsFragment(Child child, String parentName) {
        this.child = child;
        this.parentName =parentName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parent_floating_windows, container, false);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    private void setButtons() {
        Button cancelButton = view.findViewById(R.id.chancel_parent_floating_window_button);
        Button deleteButton = view.findViewById(R.id.delete_parent_floating_window_button);

        cancelButton.setOnClickListener(o -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        deleteButton.setOnLongClickListener(ol -> {
            appLab.deleteParentByIdAndName(child.getUuid(),parentName);
            AppFragmentManager.openFragment(ChildCardFragment.newInstance(child.getUuid()));
            return true;
        });
    }
}