package com.example.schoolmoney.appLab;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.schoolmoney.database.DbSchema;

public class ParentCursorWrapper extends CursorWrapper {

    public ParentCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public Child getChildWithParent(Child child){

        if (getWrappedCursor() != null && !getWrappedCursor().isClosed() && getCount() > 0) {

            moveToFirst();
            do {
                Parent parent = new Parent();
                String parentName = getString(getColumnIndex(DbSchema.ParentTable.Cols.PARENT_NAME));
                String parentPhone = getString(getColumnIndex(DbSchema.ParentTable.Cols.PARENT_PHONE));
                parent.setParentName(parentName);
                parent.setParentPhone(parentPhone);
                child.addParent(parent);
            }while (moveToNext());
        }

        return child;
    }
}
