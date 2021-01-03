package au.edu.jcu.cp3406.smartereveryday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

import au.edu.jcu.cp3406.smartereveryday.game1.NumberSeriesFragment;
import au.edu.jcu.cp3406.smartereveryday.game2.BallDropFragment;
import au.edu.jcu.cp3406.smartereveryday.utils.DatabaseHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.SystemUI;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    public static FragmentManager fragmentManager;
    public static boolean sound, online;

    private boolean isLargeScreen;
    View fragmentContainer;
    Fragment currentFragment;
    TextView daysView;
    int days, intDifficulty;
    String name, difficulty;
    String[] difficulties = {"easy", "medium", "hard", "expert"};
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SystemUI.hide(this);

        daysView = findViewById(R.id.textView_days);

        databaseHelper = new DatabaseHelper(this);

        ImageButton back = findViewById(R.id.imageButton_back);
        back.setOnClickListener(this);
        ImageButton game1 = findViewById(R.id.imageButton_game1);
        game1.setOnClickListener(this);
        ImageButton game2 = findViewById(R.id.imageButton_game2);
        game2.setOnClickListener(this);
        ImageButton info1 = findViewById(R.id.imageButton_game1_info);
        info1.setOnClickListener(this);
        ImageButton info2 = findViewById(R.id.imageButton_game2_info);
        info2.setOnClickListener(this);

        fragmentContainer = findViewById(R.id.fragment_container);

        fragmentManager = getSupportFragmentManager();

        isLargeScreen = fragmentContainer != null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (days > 1) {
            databaseHelper.insertFact(databaseHelper.getWritableDatabase(), "DAYS", days, name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUI.hide(this);
        SharedPreferences loadSh = getSharedPreferences("playerPref", Context.MODE_PRIVATE);
        intDifficulty = loadSh.getInt("difficulty", 0);
        difficulty = difficulties[intDifficulty];
        days = loadSh.getInt("days", 0);
        name = loadSh.getString("name", "");
        online = loadSh.getBoolean("online", true);
        sound = loadSh.getBoolean("sound", true);
        Log.i("date", "days loaded: " + days);
        daysView.setText(String.format(Locale.getDefault(), Integer.toString(days))); //
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            SystemUI.hide(this);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageButton_back:
                finish();
                break;
            case R.id.imageButton_game1:
                NumberSeriesFragment fragment1 = NumberSeriesFragment.newInstance(difficulty, name);
                setFrag(fragment1);
                break;
            case R.id.imageButton_game2:
                BallDropFragment fragment2 = BallDropFragment.newInstance(difficulty, name);
                setFrag(fragment2);
                break;

            case R.id.imageButton_game1_info:
                Toast.makeText(this, Html.fromHtml(
                        "<big>How to play <strong>Memory Builder: </strong></big><br>" +
                                "Memorise the sequence of numbers that appears on the screen. " +
                                "One number will be added each time.<br><br>" +
                                "(Improves: Memory, Concentration & Patience)"
                ), Toast.LENGTH_LONG).show();
                break;
            case R.id.imageButton_game2_info:
                Toast.makeText(this, Html.fromHtml(
                        "<big>How to play <strong>Ball Drop: </strong></big><br>" +
                                "Tilt your device to roll the ball into the green goal." +
                                "<br><br>" +
                                "(Improves: Motor Skills, Coordination & Spacial Awareness)"
                ), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void setFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isLargeScreen) {
            if (currentFragment != null) {
                fragmentTransaction.remove(currentFragment);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.add(R.id.game_activity, fragment);
            fragmentTransaction.commit();
        }
        currentFragment = fragment;
    }

}
