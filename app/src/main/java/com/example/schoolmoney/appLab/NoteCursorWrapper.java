package com.example.schoolmoney.appLab;
import com.example.schoolmoney.database.DbSchema.NoteTable;
import android.database.Cursor;
import android.database.CursorWrapper;

public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Child getChildWithNote(Child child){
        if (getCount()>0) {
            moveToFirst();
            String note = getString(getColumnIndex(NoteTable.Cols.NOTE));
            child.setNote(note);
        }
        return child;
    }
}
