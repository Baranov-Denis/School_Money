package com.example.schoolmoney.appLab;

import com.example.schoolmoney.database.DbSchema.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schoolmoney.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AppLab {

    private static AppLab appLab;
    private List<Child> childrenList;
    private List<Money> moneyList;
    private final SQLiteDatabase sqLiteDatabase;


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


//Добавляю ребенка в базу данных ChildTable
//Сначала генерирую UUID потом добавляю в базу UUID и childName
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

    public void addNewIncomeMoneyFromChild(UUID childUuid, String childName, int value, String date){
        ContentValues values = getContentValuesForChildMoney(childUuid,childName, value, date);
        sqLiteDatabase.insert(MoneyTable.NAME,null, values);
    }
    private ContentValues getContentValuesForChildMoney(UUID childUuid, String childName, int value, String date){
        ContentValues values = new ContentValues();
        values.put(MoneyTable.Cols.UUID,childUuid.toString());
        values.put(MoneyTable.Cols.TITLE, "Money from " + childName);
        values.put(MoneyTable.Cols.NOTE, "");
        values.put(MoneyTable.Cols.VALUE_INCOME, value);
        values.put(MoneyTable.Cols.VALUE_EXPENSES,0);
        values.put(MoneyTable.Cols.DATE,date);
        return values;
    }

    public void addNewSpendMoneyFrom(String title, String note, String spendValue, String date){
        UUID uuid = UUID.randomUUID();
        ContentValues values = getContentValuesForSpendMoney(uuid,title,note, spendValue, date);
        sqLiteDatabase.insert(MoneyTable.NAME,null, values);
    }
    private ContentValues getContentValuesForSpendMoney(UUID moneyUuid, String title, String note, String spendValue, String date){
        ContentValues values = new ContentValues();
        values.put(MoneyTable.Cols.UUID,   moneyUuid.toString());
        values.put(MoneyTable.Cols.TITLE,  title);
        values.put(MoneyTable.Cols.NOTE, "" + note);
        values.put(MoneyTable.Cols.VALUE_INCOME, 0);
        values.put(MoneyTable.Cols.VALUE_EXPENSES,spendValue);
        values.put(MoneyTable.Cols.DATE,date);
        return values;
    }

    /**
     * Получаю список детей
     *
     * @return childrenList
     */

    public List<Child> getChildrenList() {
        childrenList = new ArrayList<>();
        Child child = null;
        ChildCursorWrapper cursor = null;
        cursor = queryChildrenList(null, null);

        NoteCursorWrapper noteCursorWrapper = null;
        ParentCursorWrapper parentCursorWrapper = null;
        MoneyCursorWrapper moneyCursorWrapper = null;


        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //Получаю ребенка из курсора
                child = cursor.getChild();
                //Инициализирую курсор с заметками
                noteCursorWrapper = queryNote(NoteTable.Cols.CHILD_UUID + " = ?",
                        new String[]{child.getUuid().toString()});
                //Добавляю заметку из курсора
                child = noteCursorWrapper.getChildWithNote(child);

                //Инициализирую курсор с родителями
                parentCursorWrapper = queryParent(ParentTable.Cols.CHILD_UUID + " = ?",
                        new String[]{child.getUuid().toString()});
                //Получаю ребёнка с добавлеными родителями
                child = parentCursorWrapper.getChildWithParent(child);

                //Инициализирую курсор с деньгами
                moneyCursorWrapper = queryMoney(MoneyTable.Cols.UUID + " = ?",
                        new String[]{child.getUuid().toString()});
                //Получаю ребёнка с добавлеными деньгами
                child = moneyCursorWrapper.getChildWithMoney(child);


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

    /**
     * Получаю список денег
     *
     * @return moneyList
     */

    public List<Money> getMoneyList() {
        moneyList = new ArrayList<>();
        Money money = null;
        MoneyCursorWrapper cursorWrapper = null;
        cursorWrapper = queryMoney(null,null);




        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                //Получаю ребенка из курсора
                money = cursorWrapper.getMoney();

                if (money != null) {
                    moneyList.add(money);
                }
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        if (moneyList.size() > 1) {
            Collections.sort(moneyList);
        }
        return moneyList;
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

    private MoneyCursorWrapper queryMoney(String whereClause, String[] whereArgs){
        Cursor cursor = sqLiteDatabase.query(
                MoneyTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MoneyCursorWrapper(cursor);
    }


}
