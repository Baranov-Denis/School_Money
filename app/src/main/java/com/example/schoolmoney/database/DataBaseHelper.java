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
                ChildTable.Cols.CHILD_NAME +
                ")"
        );

        db.execSQL("create table " + NoteTable.NAME + "(" +
                NoteTable.Cols.CHILD_UUID + " primary key , " +
                NoteTable.Cols.NOTE +
                ")"
        );

        db.execSQL("create table " + ParentTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ParentTable.Cols.CHILD_UUID  + "," +
                ParentTable.Cols.PARENT_NAME + "," +
                ParentTable.Cols.PARENT_PHONE +
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
