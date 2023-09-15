package com.example.schoolmoney.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.schoolmoney.database.DbSchema.*;


import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "app_database";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
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

        db.execSQL("create table " + SettingsTable.NAME + "(" +
                SettingsTable.Cols.SETTINGS_ID + " primary key , " +
                SettingsTable.Cols.MONEY_TARGET +
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
}
