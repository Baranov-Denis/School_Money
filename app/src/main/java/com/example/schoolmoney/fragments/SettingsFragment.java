package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Settings;

public class SettingsFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private Settings settings;
    private AppCompatButton saveDatabaseButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        appLab = AppLab.getAppLab(getContext());
        settings = appLab.getSettings();
        setButtons();
        AppFragmentManager.closeApp(this);
        return view;
    }

    private void setButtons() {
        saveDatabaseButton = view.findViewById(R.id.save_database_button);
        saveDatabaseButton.setOnClickListener(o->{
            if (appLab.backUp()) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_successful_to_phone_storage), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_failed), Toast.LENGTH_LONG).show();
            }
        });
    }
}