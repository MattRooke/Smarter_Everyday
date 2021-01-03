package au.edu.jcu.cp3406.smartereveryday;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import au.edu.jcu.cp3406.smartereveryday.utils.DatabaseHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.SystemUI;

public class ScoresActivity extends AppCompatActivity {
    ListView score_name;
    ListView score_value;
    Spinner games;
    SQLiteDatabase db;
    Cursor cursor;

    SimpleCursorAdapter name1Adapter;
    SimpleCursorAdapter name2Adapter;
    SimpleCursorAdapter name3Adapter;
    SimpleCursorAdapter name4Adapter;
    SimpleCursorAdapter score1Adapter;
    SimpleCursorAdapter score2Adapter;
    SimpleCursorAdapter score3Adapter;
    SimpleCursorAdapter score4Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        SystemUI.hide(this);

        score_name = findViewById(R.id.listView_score_name);
        score_value = findViewById(R.id.listView_score_value);
        games = findViewById(R.id.spinner_games);

        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
        try {
            db = databaseHelper.getReadableDatabase();

            cursor = db.query("DAYS",
                    new String[]{"_id", "NAME", "SCORE"},
                    null, null, null, null, "SCORE DESC");
            name4Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            score4Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"SCORE"},
                    new int[]{android.R.id.text1},
                    0);

            cursor = db.query("GAME1",
                    new String[]{"_id", "NAME", "SCORE"},
                    null, null, null, null, "SCORE DESC");
            name1Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            score1Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"SCORE"},
                    new int[]{android.R.id.text1},
                    0);

            cursor = db.query("GAME2",
                    new String[]{"_id", "NAME", "SCORE"},
                    null, null, null, null, "SCORE DESC");
            name2Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            score2Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"SCORE"},
                    new int[]{android.R.id.text1},
                    0);

            cursor = db.query("GAME3",
                    new String[]{"_id", "NAME", "SCORE"},
                    null, null, null, null, "SCORE DESC");
            name3Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            score3Adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"SCORE"},
                    new int[]{android.R.id.text1},
                    0);

        } catch (SQLiteException e) {
            System.out.println(e.toString());
            Toast.makeText(this, "Database Unreachable.", Toast.LENGTH_SHORT).show();
        }

        games.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case 0:
                        score_name.setAdapter(name4Adapter);
                        score_value.setAdapter(score4Adapter);
                        break;
                    case 1:
                        score_name.setAdapter(name1Adapter);
                        score_value.setAdapter(score1Adapter);
                        break;
                    case 2:
                        score_name.setAdapter(name2Adapter);
                        score_value.setAdapter(score2Adapter);
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemUI.hide(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            SystemUI.hide(this);
        }
    }

    public void backClicked(View view) {
        finish();
    }

}
