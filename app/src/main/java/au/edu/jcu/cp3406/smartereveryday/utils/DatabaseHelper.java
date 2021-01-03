package au.edu.jcu.cp3406.smartereveryday.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smarterEveryday";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE DAYS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "SCORE INTEGER);");
            insertScore(db, "DAYS", "Brainiac", 2);
            insertScore(db, "DAYS", "Brainiac", 7);
            insertScore(db, "DAYS", "Brainiac", 14);
            insertScore(db, "DAYS", "Brainiac", 18);
            insertScore(db, "DAYS", "Brainiac", 20);

            db.execSQL("CREATE TABLE GAME1 (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "SCORE INTEGER);");
            insertScore(db, "GAME1", "Brainiac", 100);
            insertScore(db, "GAME1", "Brainiac", 1);
            insertScore(db, "GAME1", "Brainiac", 95);
            insertScore(db, "GAME1", "Brainiac", 88);
            insertScore(db, "GAME1", "Brainiac", 110);

            db.execSQL("CREATE TABLE GAME2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "SCORE INTEGER);");
            insertScore(db, "GAME2", "Brainiac", 100);
            insertScore(db, "GAME2", "Brainiac", 2);
            insertScore(db, "GAME2", "Brainiac", 95);
            insertScore(db, "GAME2", "Brainiac", 88);
            insertScore(db, "GAME2", "Brainiac", 110);

//            TODO add facts for each day of the month.
            db.execSQL("CREATE TABLE FACTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "DAY INTEGER, "
                    + "FACT TEXT);");
//            https://www.thefactsite.com/top-100-technology-facts/
            insertFact(db, "FACTS", 1, "If you look into the etymology of " +
                    "\"robot,\" it comes from the Czech word \"robota\" which translates to forced " +
                    "labor or work.");
            insertFact(db, "FACTS", 2, "A 50 petabyte(PB) hard drive could hold the entire written works of mankind, from the beginning of recorded history, in all languages.");
            insertFact(db, "FACTS", 3, "The word \"Android\" literally means a human with a male robot appearance.\n" +
                    "\n" +
                    "The female equivalent of this word is a \"Gynoid.\"");
            insertFact(db, "FACTS", 4, "Over 6000 new computer viruses are created and released every month.");
            insertFact(db, "FACTS", 5, "Facebook pays $500 and up for reporting any vulnerability in their security.");
        }
    }

    public void insertScore(SQLiteDatabase db, String TABLE, String name, int score) {
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues scoreValues = new ContentValues();
        scoreValues.put("NAME", name);
        scoreValues.put("SCORE", score);
        db.insert(TABLE, null, scoreValues);
    }

    public void insertFact(SQLiteDatabase db, String TABLE, int day, String fact) {
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues scoreValues = new ContentValues();
        scoreValues.put("DAY", day);
        scoreValues.put("FACT", fact);
        db.insert(TABLE, null, scoreValues);
    }
}
