package com.example.schoolmoney.appLab;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
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
    private final Settings settings;
    private DbxClientV2 client;

    private static DropBoxHelper dropBoxHelper;

    private DropBoxHelper(Settings settings) {
        this.settings = settings;
        if (settings.getToken() != null) {
            createDropboxClient();
        }
    }

    public static DropBoxHelper getDropboxHelper(Settings settings) {
        if (dropBoxHelper == null) {
            dropBoxHelper = new DropBoxHelper(settings);
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


    public static String getAccessToken(String authCode) {
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
        client = new DbxClientV2(config, settings.getToken());
    }

    public void uploadDatabaseToDropbox() {
        try (InputStream in = Files.newInputStream(Paths.get(Environment.getExternalStorageDirectory() + PHONE_STORAGE_FILE_PATH))) {

            try {
                // Попытайтесь получить метаданные файла
                client.files().getMetadata(DROPBOX_FILE_PATH);
                // Если файл существует, вы можете его удалить
                client.files().deleteV2(DROPBOX_FILE_PATH);
            } catch (Exception e) {
                AppLab.log("Something went wrong in uploadDatabaseToDropbox()");
            }

            client.files().uploadBuilder(DROPBOX_FILE_PATH)
                    .uploadAndFinish(in);
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
    }
}

