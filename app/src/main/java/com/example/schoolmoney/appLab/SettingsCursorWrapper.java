package com.example.schoolmoney.appLab;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.schoolmoney.database.DbSchema;

public class SettingsCursorWrapper extends CursorWrapper {

    public SettingsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Settings getSettings(){
        Settings settings = new Settings();
        if (getCount()>0) {
            moveToFirst();
            String moneyTarget = getString(getColumnIndex(DbSchema.SettingsTable.Cols.MONEY_TARGET));
            settings.setMoneyTarget(moneyTarget);
        }
        return settings;
    }
}