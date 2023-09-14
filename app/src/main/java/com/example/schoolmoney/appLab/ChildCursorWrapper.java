package com.example.schoolmoney.appLab;
import com.example.schoolmoney.database.DbSchema.ChildTable;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.schoolmoney.database.DbSchema;

import java.util.UUID;

public class ChildCursorWrapper extends CursorWrapper {


    public ChildCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Child getChild(){

        String uuidString = getString(getColumnIndex(ChildTable.Cols.UUID));
        String childNameString = getString(getColumnIndex(ChildTable.Cols.CHILD_NAME));
        String note = getString(getColumnIndex(ChildTable.Cols.NOTE));

        Child child = new Child();

        child.setUuid(UUID.fromString(uuidString));
        child.setChildName(childNameString);
        child.setNote(note);

        return child;
    }


}
