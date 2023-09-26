package com.example.schoolmoney.appLab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxOAuth1AccessToken;
import com.dropbox.core.DbxOAuth1Upgrader;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxRawClientV2;
import com.dropbox.core.v2.auth.DbxAppAuthRequests;
import com.example.schoolmoney.database.DataBaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DropBoxHelper {
    private final static String APP_KEY = "2ku3g08x0dvsxlx";
    private final static String APP_SECRET = "ehdjh83elypujnu";
    private final static String CLIENT_IDENTIFIER = "School_money";
    private final static String DROPBOX_FILE_PATH = "/school_money.db";
    private final static String PHONE_STORAGE_FILE_PATH = "/Documents/School Money/school_money.db";

    private Context context;

    private static Settings s2;
    private DbxClientV2 client;

    private static DropBoxHelper dropBoxHelper;

    private DropBoxHelper(Context context) {
        this.context = context;
        if (SharedPreferencesHelper.getData(context).getDropboxToken() != null) {
            createDropboxClient();
        }
    }

    public static DropBoxHelper getDropboxHelper(Context context) {
        if (dropBoxHelper == null) {
            dropBoxHelper = new DropBoxHelper(context);
        }
        return dropBoxHelper;
    }

    public static Intent getFirstTokenFromDropbox() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, dbxAppInfo);
// Получите URL для аутентификации
        String authorizeUrl = webAuth.authorize(DbxWebAuth.newRequestBuilder().build());
        return new Intent(Intent.ACTION_VIEW, Uri.parse(authorizeUrl));
    }


    public String getAccessToken(String authCode) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, dbxAppInfo);
        // Обменяйте код авторизации на токен OAuth2
        DbxAuthFinish authFinish = null;
        try {
            authFinish = webAuth.finishFromCode(authCode);
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        return authFinish.getAccessToken();
    }


    public void createDropboxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        client = new DbxClientV2(config, SharedPreferencesHelper.getData(context).getDropboxToken());
    }




    public boolean uploadDatabaseToDropbox() {
        try (InputStream in = Files.newInputStream(Paths.get(Environment.getExternalStorageDirectory() + PHONE_STORAGE_FILE_PATH))) {
            if(fileExist()) {
                // Если файл существует, вы можете его удалить
                client.files().deleteV2(DROPBOX_FILE_PATH);
            }
            client.files().uploadBuilder(DROPBOX_FILE_PATH)
                    .uploadAndFinish(in);

            return fileExist();
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }

    }


    public boolean fileExist(){
        try {
            // Попытайтесь получить метаданные файла
            client.files().getMetadata(DROPBOX_FILE_PATH);
            return true;
        } catch (DbxException e) {
            return false;
        }
    }

    public void downloadDatabase() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataBaseHelper.downloadDatabase(client,DROPBOX_FILE_PATH);
            }
        }).start();

    }
}

