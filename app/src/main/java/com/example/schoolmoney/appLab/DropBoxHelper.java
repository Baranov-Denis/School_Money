package com.example.schoolmoney.appLab;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DropBoxHelper {

    private final static String APP_NAME = "School_money";
    private final static String APP_KEY = "2ku3g08x0dvsxlx";
    private final static String APP_SECRET = "ehdjh83elypujnu";
    private Settings settings;

    private static DropBoxHelper dropBoxHelper;

    private DropBoxHelper(Settings settings) {
        this.settings = settings;

    }

    public static DropBoxHelper getDropboxHelper(Settings settings) {
        if (dropBoxHelper == null) {
            dropBoxHelper = new DropBoxHelper(settings);

        }
        return dropBoxHelper;
    }

    //    private final static String ACCESS_TOKEN = "sl.BmjkQmMAyxe-dASh1uSz5yMvHjXxv4exazgQHkhdK9XNWAL51LIDks2ujsenqDWNED3WKtCUMVZMB2EEpb4GbJMBFs4aw_gDCNQHFPsXe-e471kGRgEc8n6ACqkIdCVGKoWZG6olr1aN";

    // private final static String ACCESS_TOKEN = "kEdXcRTsc-MAAAAAAAAAFjxPcTQt8wtjHx3mrqe862c";
    public static Intent start() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("School_money").build();
        DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, dbxAppInfo);
// Получите URL для аутентификации
        String authorizeUrl = webAuth.authorize(DbxWebAuth.newRequestBuilder().build());
        return new Intent(Intent.ACTION_VIEW, Uri.parse(authorizeUrl));
    }


    public static String getAccessToken(String authCode) throws DbxException, IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("School_money").build();
        DbxAppInfo dbxAppInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, dbxAppInfo);

        // Обменяйте код авторизации на токен OAuth2
        DbxAuthFinish authFinish = webAuth.finishFromCode(authCode);
        return authFinish.getAccessToken();
    }

    public void createDropboxClient() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(APP_NAME).build();
        DbxClientV2 client = new DbxClientV2(config, settings.getToken());
        FullAccount account = client.users().getCurrentAccount();
        AppLab.log(account.getName().getDisplayName());



        try (InputStream in = new FileInputStream( Environment.getExternalStorageDirectory() + "/Documents/School Money/school_money.db")) {
            FileMetadata metadata = client.files().uploadBuilder("/Test/school_money.db")
                    .uploadAndFinish(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

