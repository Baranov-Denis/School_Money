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
import com.dropbox.core.android.Auth;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.oauth.DbxRefreshResult;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxRawClientV2;
import com.dropbox.core.v2.auth.DbxAppAuthRequests;
import com.example.schoolmoney.R;
import com.example.schoolmoney.database.DataBaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DropBoxHelper {
    private final static String APP_KEY = "2ku3g08x0dvsxlx";
    private final static String APP_SECRET = "ehdjh83elypujnu";
    private final static String CLIENT_IDENTIFIER = "School_money";
    private final static String DROPBOX_FILE_PATH = "/school_money.db";
    private final static String PHONE_STORAGE_FILE_PATH = "/Documents/School Money/school_money.db";


    private Context context;
    private DbxClientV2 client;
    private static DropBoxHelper dropBoxHelper;

    public DbxClientV2 getClient() {
        return client;
    }

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

        AppLab.log(webAuth.authorize(DbxWebAuth.newRequestBuilder().build()));
        //String authorizeUrl = "https://www.dropbox.com/oauth2/authorize?response_type=code&token_access_type=offline&client_id=2ku3g08x0dvsxlx";
        String authorizeUrl = "https://www.dropbox.com/oauth2/authorize?response_type=code&token_access_type=offline&client_id=" + APP_KEY;
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
        SharedPreferencesHelper.saveRefreshToken(context, authFinish.getRefreshToken());
        SharedPreferencesHelper.saveExpiresTime(context, authFinish.getExpiresAt().toString());

        return authFinish.getAccessToken();

    }


    public boolean refreshAccessToken() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        String oldToken = SharedPreferencesHelper.getData(context).getDropboxToken();
        String refToken = SharedPreferencesHelper.getData(context).getRefreshToken();
        Long exp = Long.parseLong(SharedPreferencesHelper.getData(context).getExpiresTime());
        DbxCredential dbxCredential = new DbxCredential(oldToken, exp, refToken, APP_KEY, APP_SECRET);
        client = new DbxClientV2(config, dbxCredential);

        try {
            DbxRefreshResult refreshResult = client.refreshAccessToken();
            SharedPreferencesHelper.saveToken(context, refreshResult.getAccessToken());
        } catch (DbxException e) {
            return false;
        }
        client = new DbxClientV2(config, SharedPreferencesHelper.getData(context).getDropboxToken());
        return true;
    }


    public void createDropboxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        client = new DbxClientV2(config, SharedPreferencesHelper.getData(context).getDropboxToken());
    }


    public boolean uploadDatabaseToDropbox() {
        try (InputStream in = Files.newInputStream(Paths.get(Environment.getExternalStorageDirectory() + PHONE_STORAGE_FILE_PATH))) {
            if (!fileExist()) {
                refreshAccessToken();
            }
            if (fileExist()) {
                // Если файл существует, вы можете его удалить
                client.files().deleteV2(DROPBOX_FILE_PATH);
            }

            client.files().uploadBuilder(DROPBOX_FILE_PATH)
                    .uploadAndFinish(in);

            return fileExist();
        } catch (IOException | DbxException e) {
            AppLab.log("uploadDatabaseToDropbox() failed");
            throw new RuntimeException(e);
        }

    }


    public boolean fileExist() {
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
                if (!fileExist()) {
                    refreshAccessToken();
                }
                dataBaseHelper.downloadDatabase(client, DROPBOX_FILE_PATH);
            }
        }).start();

    }
}

