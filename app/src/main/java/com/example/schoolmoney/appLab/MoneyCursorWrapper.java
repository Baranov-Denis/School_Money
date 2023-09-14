package com.example.schoolmoney.appLab;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.schoolmoney.database.DbSchema;

import java.sql.DatabaseMetaData;
import java.util.UUID;

public class MoneyCursorWrapper extends CursorWrapper {

    public MoneyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Child getChildWithMoney(Child child){
        if (getWrappedCursor() != null && !getWrappedCursor().isClosed() && getCount() > 0) {
            moveToFirst(); // Переместить курсор на первую запись
            do {
                Money money = new Money();
                String title = getString(getColumnIndex(DbSchema.MoneyTable.Cols.TITLE));
                UUID moneyUuid = UUID.fromString(getString(getColumnIndex(DbSchema.MoneyTable.Cols.MONEY_UUID)));
                String note = getString(getColumnIndex(DbSchema.MoneyTable.Cols.NOTE));
                String valueIncome = getString(getColumnIndex(DbSchema.MoneyTable.Cols.VALUE_INCOME));
                String valueExpenses = getString(getColumnIndex(DbSchema.MoneyTable.Cols.VALUE_EXPENSES));
                String date = getString(getColumnIndex(DbSchema.MoneyTable.Cols.DATE));
                money.setTitle(title);
                money.setMoneyUuid(moneyUuid);
                money.setNote(note);
                money.setValueIncome(valueIncome);
                money.setValueExpenses(valueExpenses);
                money.setDate(date);
                child.addMoney(money);
            } while (moveToNext()); // Используйте do-while для чтения первой записи и затем следующих

        }
        return child;
    }

    public Money getMoney(){
        Money money = new Money();
        String title = getString(getColumnIndex(DbSchema.MoneyTable.Cols.TITLE));
        String moneyUuid = getString(getColumnIndex(DbSchema.MoneyTable.Cols.MONEY_UUID));
        String note = getString(getColumnIndex(DbSchema.MoneyTable.Cols.NOTE));
        String valueIncome = getString(getColumnIndex(DbSchema.MoneyTable.Cols.VALUE_INCOME));
        String valueExpenses = getString(getColumnIndex(DbSchema.MoneyTable.Cols.VALUE_EXPENSES));
        String date = getString(getColumnIndex(DbSchema.MoneyTable.Cols.DATE));
        money.setTitle(title);
        money.setMoneyUuid(UUID.fromString(moneyUuid));
        money.setNote(note);
        money.setValueIncome(valueIncome);
        money.setValueExpenses(valueExpenses);
        money.setDate(date);
        return money;
    }
}
