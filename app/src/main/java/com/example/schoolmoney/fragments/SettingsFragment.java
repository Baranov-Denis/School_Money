package com.example.schoolmoney.fragments;

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

public class SettingsFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private Settings settings;
    private AppCompatButton saveDatabaseButton;
    private AppCompatButton getFirstDropboxTokenDropboxButton;
    private AppCompatButton saveTokenButton;
    private EditText setTokenEditText;

    private AppCompatButton saveToDropBoxButton;

    private DropBoxHelper dropBoxHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        appLab = AppLab.getAppLab(getContext());
        settings = appLab.getSettings();

        setButtons();
        AppFragmentManager.createBottomButtons();
        dropBoxHelper = DropBoxHelper.getDropboxHelper(settings);
       // AppFragmentManager.closeApp(this);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startActivity(DropBoxHelper.getFirstTokenFromDropbox());
                }
            }).start();
        });





        saveToDropBoxButton.setOnClickListener(o->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Ваш код, который требует интернет-соединения
                        // Например, попытка выполнить операции с Dropbox
                       // DropBoxHelper.getDropboxHelper(appLab.getSettings()).createDropboxClient();
                        dropBoxHelper.uploadDatabaseToDropbox();
                    } catch (Exception e) {
                     AppLab.log("internet fail");
                        //Toast.makeText(getContext(), "NetworkOnMainThreadException", Toast.LENGTH_LONG).show();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "NetworkOnMainThreadException", Toast.LENGTH_LONG).show();
                            }
                        });
                        // Обработка ошибки отсутствия интернет-соединения
                        // Здесь можно выполнить действия, чтобы сообщить пользователю о проблеме
                        // Например, показать диалоговое окно с предупреждением
                        // или отобразить сообщение об ошибке в интерфейсе пользователя
                        e.printStackTrace(); // Это позволяет записать информацию об ошибке в логи для отладки
                    }

                }
            }).start();
        });

        saveTokenButton.setOnClickListener(o->{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    appLab.addToken(DropBoxHelper.getAccessToken(setTokenEditText.getText().toString()));
                }
            }).start();

        });
    }

}


