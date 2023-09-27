package com.example.schoolmoney.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.DropBoxHelper;
import com.example.schoolmoney.appLab.Settings;
import com.example.schoolmoney.appLab.SharedPreferencesHelper;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private View view;
    private AppLab appLab;

    private AppCompatButton saveDatabaseButton;
    private AppCompatButton getFirstDropboxTokenDropboxButton;
    private AppCompatButton saveTokenButton;
    private EditText setTokenEditText;
    private AppCompatButton saveToDropBoxButton;
    private AppCompatButton downloadDatabaseFromDropboxButton;

    private DropBoxHelper dropBoxHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        appLab = AppLab.getAppLab(getContext());


        setButtons();
        AppFragmentManager.createBottomButtons();
        dropBoxHelper = DropBoxHelper.getDropboxHelper(getContext());
        AppLab.log(SharedPreferencesHelper.getData(getContext()).getDropboxToken());
        return view;
    }

    private void setButtons() {
        saveDatabaseButton = view.findViewById(R.id.save_database_button);
        getFirstDropboxTokenDropboxButton = view.findViewById(R.id.get_first_token_dropbox_button);
        setTokenEditText = view.findViewById(R.id.set_token_edit);
        saveTokenButton = view.findViewById(R.id.save_token_button);
        saveToDropBoxButton = view.findViewById(R.id.save_database_to_dropbox_button);
        downloadDatabaseFromDropboxButton = view.findViewById(R.id.download_database_from_dropbox_button);



        saveDatabaseButton.setOnClickListener(o -> {
            if (appLab.backUp()) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_successful_to_phone_storage), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_failed), Toast.LENGTH_LONG).show();
            }
        });


        getFirstDropboxTokenDropboxButton.setOnClickListener(o -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startActivity(DropBoxHelper.getFirstTokenFromDropbox());
                }
            }).start();
        });



        saveToDropBoxButton.setOnClickListener(o->{
            appLab.saveDataBaseToDropbox(getContext(),getActivity(),dropBoxHelper);
        });

        saveTokenButton.setOnClickListener(o->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!setTokenEditText.getText().toString().equals("")) {
                        SharedPreferencesHelper.saveToken(requireContext(), dropBoxHelper.getAccessToken(setTokenEditText.getText().toString()));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), requireActivity().getResources().getString(R.string.Token_was_successfully_updated), Toast.LENGTH_LONG).show();
                                setTokenEditText.setText("");
                            }
                        });
                        //
                    }else {
                        refreshToken();
                    }
                }
            }).start();

        });

        downloadDatabaseFromDropboxButton.setOnClickListener(o->{
            dropBoxHelper.downloadDatabase();

        });


    }

    private void refreshToken(){
       if(dropBoxHelper.refreshAccessToken()){
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(getContext(), requireActivity().getResources().getString(R.string.Token_was_successfully_updated), Toast.LENGTH_LONG).show();
               }
           });
       }
    }



}


