package com.example.schoolmoney.appLab;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class SharedPreferencesHelper {

        // Имя файла SharedPreferences
        private static final String PREFS_NAME = "MyPrefsFile";
        // Ключи для сохранения и получения данных
        private static final String DROPBOX_TOKEN = "dropbox_token";
        private static final String MONEY_TARGET = "money_target";
        private static final String REFRESH_TOKEN = "refresh_token";
        private static final String EXPIRES_TIME = "expires_time";

        // Метод для сохранения данных
        public static void saveData(Context context, String dropbox_token, String money_target) {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(DROPBOX_TOKEN, dropbox_token);
            editor.putString(MONEY_TARGET, money_target);
            editor.apply();
        }

    public static void saveToken(Context context, String dropbox_token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(DROPBOX_TOKEN, dropbox_token);
        editor.apply();
    }

    public static void saveRefreshToken(Context context, String refresh_token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(REFRESH_TOKEN, refresh_token);
        editor.apply();
    }

    public static void saveExpiresTime(Context context, String expires_time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(EXPIRES_TIME, expires_time);
        editor.apply();
    }

    public static void saveMoneyTarget(Context context, String money_target) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(MONEY_TARGET, money_target);
        editor.apply();
    }

        // Метод для получения данных
        public static UserData getData(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String dropboxToken = prefs.getString(DROPBOX_TOKEN, "");
            String moneyTarget = prefs.getString(MONEY_TARGET, "");
            String refreshToken = prefs.getString(REFRESH_TOKEN,"");
            String expiresTime = prefs.getString(EXPIRES_TIME,"");
            return new UserData(dropboxToken, moneyTarget,refreshToken,expiresTime);
        }

        // Класс для хранения данных
        public static class UserData {
            private String dropboxToken;
            private String moneyTarget;
            private String refreshToken;
            private String expiresTime;

            public UserData(String dropboxToken, String moneyTarget, String refreshToken, String expiresTime) {
                this.dropboxToken = dropboxToken;
                this.moneyTarget = moneyTarget;
                this.refreshToken = refreshToken;
                this.expiresTime = expiresTime;
            }

            public String getRefreshToken() {
                return refreshToken;
            }
            public String getExpiresTime() {
                return expiresTime;
            }
            public String getDropboxToken() {
                return dropboxToken;
            }
            public String getMoneyTarget() {
                if(Objects.equals(moneyTarget, ""))moneyTarget = "1";
                return moneyTarget;
            }
        }


}
