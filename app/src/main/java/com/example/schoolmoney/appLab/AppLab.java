package com.example.schoolmoney.appLab;


import com.example.schoolmoney.database.DbSchema.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.schoolmoney.database.DataBaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AppLab {

    public static final String GLOBAL_TAG = "global_tag";
    private static AppLab appLab;
    private List<Child> childrenList;
    private List<Money> moneyList;
    private Settings settings;
    private final SQLiteDatabase sqLiteDatabase;
    private int childPosition;
    private int moneyPosition;

    public int getMoneyPosition() {
        return moneyPosition;
    }

    public void setMoneyPosition(int moneyPosition) {
        this.moneyPosition = moneyPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

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
        sqLiteDatabase.insert(ChildTable.NAME, null, values);
    }

    private ContentValues getContentValuesForChildrenName(UUID uuid, String childName) {
        ContentValues values = new ContentValues();
        values.put(ChildTable.Cols.UUID, uuid.toString());
        values.put(ChildTable.Cols.CHILD_NAME, childName);
        values.put(ChildTable.Cols.NOTE, "");
        return values;
    }

    public void addNote(Child child, String note) {
        ContentValues values = getContentValuesForNote(child, note);
        sqLiteDatabase.insertWithOnConflict(
                ChildTable.NAME,       // Имя таблицы
                null,                // nullColumnHack (обычно null)
                values,              // Данные для вставки
                SQLiteDatabase.CONFLICT_REPLACE // Заменить существующую запись при конфликте
        );
    }

    private ContentValues getContentValuesForNote(Child child, String note) {
        ContentValues values = new ContentValues();
        values.put(ChildTable.Cols.UUID, child.getUuid().toString());
        values.put(ChildTable.Cols.CHILD_NAME, child.getChildName());
        values.put(ChildTable.Cols.NOTE, note);
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

    public void addNewIncomeMoneyFromChild(UUID moneyUuid, UUID childUuid, String childName, int value, String date) {
        ContentValues values = getContentValuesForChildMoney(moneyUuid, childUuid, childName, value, date);
        sqLiteDatabase.insert(MoneyTable.NAME, null, values);
    }

    private ContentValues getContentValuesForChildMoney(UUID moneyUuid, UUID childUuid, String childName, int value, String date) {
        ContentValues values = new ContentValues();
        values.put(MoneyTable.Cols.MONEY_UUID, moneyUuid.toString());
        values.put(MoneyTable.Cols.CHILD_UUID, childUuid.toString());
        values.put(MoneyTable.Cols.TITLE, "Money from " + childName);
        values.put(MoneyTable.Cols.NOTE, "");
        values.put(MoneyTable.Cols.VALUE_INCOME, value);
        values.put(MoneyTable.Cols.VALUE_EXPENSES, 0);
        values.put(MoneyTable.Cols.DATE, date);
        return values;
    }

    public void addNewSpendMoneyFrom(String title, String note, String spendValue, String date) {
        UUID uuid = UUID.randomUUID();
        ContentValues values = getContentValuesForSpendMoney(uuid, title, note, spendValue, date);
        sqLiteDatabase.insert(MoneyTable.NAME, null, values);
    }

    private ContentValues getContentValuesForSpendMoney(UUID moneyUuid, String title, String note, String spendValue, String date) {
        ContentValues values = new ContentValues();
        values.put(MoneyTable.Cols.MONEY_UUID, moneyUuid.toString());
        values.put(MoneyTable.Cols.CHILD_UUID, UUID.randomUUID().toString());
        values.put(MoneyTable.Cols.TITLE, title);
        values.put(MoneyTable.Cols.NOTE, "" + note);
        values.put(MoneyTable.Cols.VALUE_INCOME, 0);
        values.put(MoneyTable.Cols.VALUE_EXPENSES, spendValue);
        values.put(MoneyTable.Cols.DATE, date);
        return values;
    }




    /**
     * Удаление данных из базы данных
     */

    //Удаляет только указанного родителя для указанного ребенка
    public void deleteParentByIdAndName(UUID childId, String parentName) {
        String whereClause = ParentTable.Cols.CHILD_UUID + " = ? AND " + ParentTable.Cols.PARENT_NAME + " = ?";
        String[] whereArgs = new String[]{childId.toString(), parentName};
        sqLiteDatabase.delete(ParentTable.NAME, whereClause, whereArgs);
    }

    //Удаляет всех родителей для указанного ребенка
    public void deleteParentById(UUID childId) {
        sqLiteDatabase.delete(ParentTable.NAME, ParentTable.Cols.CHILD_UUID + "= ?", new String[]{childId.toString()});
    }


    public void deleteMoneyByMoneyId(UUID moneyUuid) {
        sqLiteDatabase.delete(MoneyTable.NAME, MoneyTable.Cols.MONEY_UUID + "= ?", new String[]{moneyUuid.toString()});
    }

    public void deleteChildByUuid(UUID uuid) {
        deleteParentById(uuid);
        sqLiteDatabase.delete(ChildTable.NAME, ChildTable.Cols.UUID + "= ?", new String[]{uuid.toString()});
    }

    /**
     * Замена в базе данных
     */

    public void changeNoteAndName(Child child, String name, String note) {
        ContentValues contentValues = getContentValuesForChangeChild(child, name, note);
        sqLiteDatabase.insertWithOnConflict(
                ChildTable.NAME,       // Имя таблицы
                null,                // nullColumnHack (обычно null)
                contentValues,              // Данные для вставки
                SQLiteDatabase.CONFLICT_REPLACE // Заменить существующую запись при конфликте
        );
    }

    private ContentValues getContentValuesForChangeChild(Child child, String name, String note) {
        ContentValues values = new ContentValues();
        values.put(ChildTable.Cols.UUID, child.getUuid().toString());
        values.put(ChildTable.Cols.CHILD_NAME, name);
        values.put(ChildTable.Cols.NOTE, note);
        return values;
    }

    public void changeMoneyTitleAndNote(Money money, String title, String note) {
        ContentValues contentValues = getContentValuesForChangeSpendMoney(money, title, note);
        sqLiteDatabase.insertWithOnConflict(
                MoneyTable.NAME,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    private ContentValues getContentValuesForChangeSpendMoney(Money money, String title, String note) {
        ContentValues values = new ContentValues();
        values.put(MoneyTable.Cols.MONEY_UUID, money.getMoneyUuid().toString());
        values.put(MoneyTable.Cols.VALUE_INCOME, money.getValueIncome());
        values.put(MoneyTable.Cols.VALUE_EXPENSES, money.getValueExpenses());
        values.put(MoneyTable.Cols.CHILD_UUID, money.getChildUuid().toString());
        values.put(MoneyTable.Cols.DATE, money.getDate());
        values.put(MoneyTable.Cols.TITLE, title);
        values.put(MoneyTable.Cols.NOTE, note);
        return values;
    }

    public void addSettings(String moneyTarget){
        ContentValues contentValues = getSettingContentValues(moneyTarget);
        sqLiteDatabase.insertWithOnConflict(
                SettingsTable.NAME,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    private ContentValues getSettingContentValues(String moneyTarget) {
        ContentValues values = new ContentValues();
        values.put(SettingsTable.Cols.SETTINGS_ID,"settingsId");
        values.put(SettingsTable.Cols.MONEY_TARGET,moneyTarget);
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
            /*    //Инициализирую курсор с заметками
                noteCursorWrapper = queryNote(NoteTable.Cols.CHILD_UUID + " = ?",
                        new String[]{child.getUuid().toString()});
                //Добавляю заметку из курсора
                child = noteCursorWrapper.getChildWithNote(child);*/

                //Инициализирую курсор с родителями
                parentCursorWrapper = queryParent(ParentTable.Cols.CHILD_UUID + " = ?",
                        new String[]{child.getUuid().toString()});
                //Получаю ребёнка с добавлеными родителями
                child = parentCursorWrapper.getChildWithParent(child);

                //Инициализирую курсор с деньгами
                moneyCursorWrapper = queryMoney(MoneyTable.Cols.CHILD_UUID + " = ?",
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
        cursorWrapper = queryMoney(null, null);


        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                //Получаю money из курсора
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

    public Settings getSettings(){
        settings = new Settings();
        SettingsCursorWrapper settingsCursorWrapper = null;
        settingsCursorWrapper = querySettings(null,null);

        try {
            settingsCursorWrapper.moveToFirst();
           // while (!settingsCursorWrapper.isAfterLast()) {
                //Получаю money из курсора
                settings = settingsCursorWrapper.getSettings();

             //   settingsCursorWrapper.moveToNext();
          //  }
        } finally {
            settingsCursorWrapper.close();
        }
        return settings;
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

    public Money getMoneyById(UUID moneyUuid) {
        moneyList = getMoneyList();
        for (Money money : moneyList) {
            if (money.getMoneyUuid().equals(moneyUuid)) return money;
        }
        return null;
    }


    /*  private NoteCursorWrapper queryNote(String whereClause, String[] whereArgs) {
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
  */
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

    private MoneyCursorWrapper queryMoney(String whereClause, String[] whereArgs) {
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

    private SettingsCursorWrapper querySettings(String whereClause, String[] whereArgs){
        Cursor cursor = sqLiteDatabase.query(
                SettingsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new SettingsCursorWrapper(cursor);
    }


    public boolean backUp() {
        try {
            File data = Environment.getDataDirectory();
            String state = Environment.getExternalStorageState();
            // Проверка и запрос разрешений
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                String currentDBPath = "/data/com.example.schoolmoney/databases/app_database";
                File currentDB = new File(data, currentDBPath);
                File on = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "School Money");
                on.mkdirs();
                // Имя файла
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                String formattedDate = currentDate.format(formatter);
                StringBuilder outputFileName = new StringBuilder("/school_money_");
                outputFileName.append(formattedDate);
                outputFileName.append(".db");
                File backupDBFile = new File(on, outputFileName.toString());
                if (currentDB.exists()) {

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDBFile).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    return true; // Успешное резервное копирование
                }
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.i(AppLab.GLOBAL_TAG, "sdcard mounted readonly");
            } else {
                Log.i(AppLab.GLOBAL_TAG, "sdcard state: " + state);
            }
        } catch (IOException e) {
            Log.i(AppLab.GLOBAL_TAG, "Error during backup: " + e.getMessage());
        }
        return false; // Ошибка резервного копирования
    }

}
