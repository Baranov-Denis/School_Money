package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Settings;

import java.io.IOException;

public class SettingsFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private Settings settings;
    private AppCompatButton saveDatabaseButton;
    private AppCompatButton sendToDropboxButton;

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
        sendToDropboxButton = view.findViewById(R.id.send_database_to_dropbox_button);
        saveDatabaseButton.setOnClickListener(o->{
            if (appLab.backUp()) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_successful_to_phone_storage), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_failed), Toast.LENGTH_LONG).show();
            }
        });

/*
        sendToDropboxButton.setOnClickListener(o->{
            DbxRequestConfig config = DbxRequestConfig.newBuilder("School_money").build();
            DbxAppInfo appInfo = new DbxAppInfo("2ku3g08x0dvsxlx", "ehdjh83elypujnu");
            DbxWebAuth auth = new DbxWebAuth(config, appInfo);

            // Генерация URL для аутентификации
            String authorizeUrl = auth.authorize(DbxWebAuth.newRequestBuilder()
                    .withNoRedirect()
                    .build());
            // Обработка кода аутентификации
            DbxAuthFinish authFinish;
            try {
                authFinish = auth.finishFromRedirect("myapp://auth/callback", params);
            } catch (DbxException | IOException e) {
                // Обработка ошибок
            }

// Получение токена доступа
            String accessToken = authFinish.getAccessToken();
        });*/
    }
}