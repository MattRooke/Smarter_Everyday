package au.edu.jcu.cp3406.smartereveryday.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import au.edu.jcu.cp3406.smartereveryday.MainActivity;

public class FactHelper {

    public String getFact() {
        int day = MainActivity.dateHelper.getCurrentDay();
        SQLiteDatabase db = MainActivity.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("FACTS",
                new String[]{"_id", "DAY", "FACT"},
                null, null, null, null, "DAY ASC");
        if (cursor.moveToPosition(day)) {
            return cursor.getString(cursor.getColumnIndex("FACT"));
        } else {
            cursor.close();
            return "This is a great app!";
        }
    }

}
