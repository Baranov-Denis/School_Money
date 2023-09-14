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
import com.example.schoolmoney.fragments.ChildrenPageFragment;

import java.util.UUID;


public class DeleteChildFloatingWindowFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private Child child;

    public DeleteChildFloatingWindowFragment(Child child) {
        this.child = child;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delete_child_floating_window, container, false);
        appLab = AppLab.getAppLab(getContext());
        setButtons();
        return view;
    }

    private void setButtons() {
        Button cancelButton = view.findViewById(R.id.chancel_child_floating_window_button);
        Button deleteButton = view.findViewById(R.id.delete_child_floating_window_button);

        cancelButton.setOnClickListener(o -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        deleteButton.setOnLongClickListener(ol -> {
            appLab.deleteChildByUuid(child.getUuid());
            AppFragmentManager.openFragment(new ChildrenPageFragment());
            return true;
        });
    }
}