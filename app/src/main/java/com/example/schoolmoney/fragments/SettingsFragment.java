package com.example.schoolmoney.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.DropBoxHelper;
import com.example.schoolmoney.appLab.Settings;

import java.io.IOException;

public class SettingsFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private Settings settings;
    private AppCompatButton saveDatabaseButton;
    private AppCompatButton getFirstDropboxTokenDropboxButton;
    private AppCompatButton saveTokenButton;
    private EditText setTokenEditText;

    private AppCompatButton saveToDropBoxButton;

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
        getFirstDropboxTokenDropboxButton = view.findViewById(R.id.get_first_token_dropbox_button);
        setTokenEditText = view.findViewById(R.id.set_token_edit);
        saveTokenButton = view.findViewById(R.id.save_token_button);
        saveToDropBoxButton = view.findViewById(R.id.save_database_to_dropbox_button);



        saveDatabaseButton.setOnClickListener(o -> {
            if (appLab.backUp()) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_successful_to_phone_storage), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.Backup_is_failed), Toast.LENGTH_LONG).show();
            }
        });


        getFirstDropboxTokenDropboxButton.setOnClickListener(o -> {
            try {
                startActivity(DropBoxHelper.start());
            } catch (DbxException e) {
                throw new RuntimeException(e);
            }

        });

        saveToDropBoxButton.setOnClickListener(o->{
                new CreateDropboxLab(appLab).execute();
        });

        saveTokenButton.setOnClickListener(o->{
            GetToken getToken = new GetToken(appLab , setTokenEditText.getText().toString());
            getToken.execute();
        });
    }

}




class GetToken extends AsyncTask<Void, Void, Void> {
private AppLab appLab;
private String token;

    public GetToken(AppLab appLab, String token) {
        this.appLab = appLab;
        this.token = token;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Выполните сетевые операции здесь
        try {
            appLab.addToken(DropBoxHelper.getAccessToken(token));
        } catch (DbxException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Обработайте результат сетевых операций (если необходимо)
    }
}


class CreateDropboxLab extends AsyncTask<Void, Void, Void> {
    private AppLab appLab;

    public CreateDropboxLab(AppLab appLab) {
        this.appLab = appLab;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Выполните сетевые операции здесь
        try {
            DropBoxHelper.getDropboxHelper(appLab.getSettings()).createDropboxClient();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Обработайте результат сетевых операций (если необходимо)
    }
}



