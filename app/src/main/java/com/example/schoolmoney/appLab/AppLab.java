package com.example.schoolmoney.appLab;

import com.example.schoolmoney.database.DbSchema.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.schoolmoney.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AppLab {

    private static AppLab appLab;

    private List<Child> childrenList;
    private SQLiteDatabase sqLiteDatabase;


    private AppLab(Context context) {
        context = context.getApplicationContext();
        sqLiteDatabase = new DataBaseHelper(context).getWritableDatabase();
    }


    public static AppLab getAppLab(Context context) {
        if (appLab == null) {
            appLab = new AppLab(context);
        }
        return appLab;
    }


    /**
     * Добавление в таблицы баз данных
     */


    public void addNewChild(String childName) {
        UUID uuid = UUID.randomUUID();
        ContentValues values = getContentValuesForChildrenName(uuid, childName);
        //   if (getPasswordCardByUUID(uuid) == null) {
        sqLiteDatabase.insert(ChildTable.NAME, null, values);
        //  }
    }

    private ContentValues getContentValuesForChildrenName(UUID uuid, String childName) {
        ContentValues values = new ContentValues();
        values.put(ChildTable.Cols.UUID, uuid.toString());
        values.put(ChildTable.Cols.CHILD_NAME, childName);
        return values;
    }


    public void addNote(UUID uuid, String note) {
        ContentValues values = getContentValuesForNote(uuid, note);

        sqLiteDatabase.insertWithOnConflict(
                NoteTable.NAME,       // Имя таблицы
                null,                // nullColumnHack (обычно null)
                values,              // Данные для вставки
                SQLiteDatabase.CONFLICT_REPLACE // Заменить существующую запись при конфликте
        );
    }

    private ContentValues getContentValuesForNote(UUID uuid, String note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.CHILD_UUID, uuid.toString());
        values.put(NoteTable.Cols.NOTE, note);
        return values;
    }

    public void addNewParent(UUID uuid, String parentName, String parentPhone) {
        ContentValues values = getContentValuesForChildrenName(uuid, parentName, parentPhone);
        sqLiteDatabase.insert(ParentTable.NAME, null, values);
    }

    private ContentValues getContentValuesForChildrenName(UUID uuid, String parentName, String parentPhone) {
        ContentValues values = new ContentValues();
        values.put(ParentTable.Cols.CHILD_UUID, uuid.toString());
        values.put(ParentTable.Cols.PARENT_NAME, parentName);
        values.put(ParentTable.Cols.PARENT_PHONE, parentPhone);
        return values;
    }


    /**
     * Получаю список детей
     *
     * @return
     */

    public List<Child> getChildrenList() {
        childrenList = new ArrayList<>();
        Child child = null;
        ChildCursorWrapper cursor = null;
        cursor = queryChildrenList(null, null);

        NoteCursorWrapper noteCursorWrapper = null;
        ParentCursorWrapper parentCursorWrapper = null;


        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //Получаю ребенка из курсора
                child = cursor.getChild();
                //Делаю запрос для получения курсора с заметками
                noteCursorWrapper = queryNote(NoteTable.Cols.CHILD_UUID + " = ?",
                        new String[]{child.getUuid().toString()});
                //Добавляю заметку из курсора
                child = noteCursorWrapper.getChildWithNote(child);

                parentCursorWrapper = queryParent(ParentTable.Cols.CHILD_UUID + " = ?",
                        new String[]{child.getUuid().toString()});

                child = parentCursorWrapper.getChildWithParent(child);


                if (child != null) {
                    childrenList.add(child);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        if (childrenList.size() > 1) {
            Collections.sort(childrenList);
        }
        return childrenList;
    }


    public Child getChildByUUID(UUID uuid) {
        childrenList = getChildrenList();
        for (Child child : childrenList) {
            if (child.getUuid().equals(uuid)) return child;
        }
        return null;
    }


    private ChildCursorWrapper queryChildrenList(String whereClause, String[] whereArgs) {
        Cursor cursor = sqLiteDatabase.query(
                ChildTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ChildCursorWrapper(cursor);
    }


    private NoteCursorWrapper queryNote(String whereClause, String[] whereArgs) {
        Cursor cursor = sqLiteDatabase.query(
                NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new NoteCursorWrapper(cursor);
    }

    private ParentCursorWrapper queryParent(String whereClause, String[] whereArgs) {
        Cursor cursor = sqLiteDatabase.query(
                ParentTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ParentCursorWrapper(cursor);
    }


}
