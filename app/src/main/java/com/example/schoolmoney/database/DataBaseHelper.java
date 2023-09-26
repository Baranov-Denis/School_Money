package com.example.schoolmoney.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.database.DbSchema.*;
import com.example.schoolmoney.fragments.AppFragmentManager;
import com.example.schoolmoney.fragments.ChildrenPageFragment;


import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "app_database";

    private Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + ChildTable.NAME + "(" +
                ChildTable.Cols.UUID + " primary key , " +
                ChildTable.Cols.CHILD_NAME + "," +
                ChildTable.Cols.NOTE +
                ")"
        );


        db.execSQL("create table " + ParentTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ParentTable.Cols.CHILD_UUID  + "," +
                ParentTable.Cols.PARENT_NAME + "," +
                ParentTable.Cols.PARENT_PHONE +
                ")"
        );

        db.execSQL("create table " + MoneyTable.NAME + "(" +
                MoneyTable.Cols.MONEY_UUID + " primary key , " +
                MoneyTable.Cols.CHILD_UUID + "," +
                MoneyTable.Cols.TITLE + "," +
                MoneyTable.Cols.NOTE + "," +
                MoneyTable.Cols.VALUE_INCOME + "," +
                MoneyTable.Cols.VALUE_EXPENSES + "," +
                MoneyTable.Cols.DATE +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void downloadDatabase(DbxClientV2 dropboxClient, String dropboxPath) {
        try {
            File localDatabase = context.getDatabasePath(DATABASE_NAME);

            // Удаляем существующую базу данных, если она существует
            if (localDatabase.exists()) {
                localDatabase.delete();
            }
            AppLab.log(dropboxClient.files().toString());

            // Загружаем базу данных из Dropbox

            DbxDownloader<FileMetadata> downloader = dropboxClient.files().download(dropboxPath);

            try (OutputStream out = new FileOutputStream(localDatabase)) {
                downloader.download(out);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        AppLab.resetAppLab();
        AppFragmentManager.openFragment(new ChildrenPageFragment());
    }
}
