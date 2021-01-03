package au.edu.jcu.cp3406.smartereveryday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import au.edu.jcu.cp3406.smartereveryday.audio.AudioManager;
import au.edu.jcu.cp3406.smartereveryday.audio.Sound;
import au.edu.jcu.cp3406.smartereveryday.utils.DatabaseHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.DateHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.FactHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.SystemUI;

public class MainActivity extends AppCompatActivity {
    public static DateHelper dateHelper;
    public static DatabaseHelper databaseHelper;
    public static AudioManager audioManager;

    static final String DEFAULT_NAME = "Brainiac";
    FactHelper factHelper;
    TextView fact, playerNameInput;
    String playerName = DEFAULT_NAME;
    int daysInRow;
    long lastLogin;
    boolean sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemUI.hide(this);

        audioManager = new AudioManager(this);

        databaseHelper = new DatabaseHelper(this);

        dateHelper = new DateHelper();

        playerNameInput = findViewById(R.id.editText_player_name);
        playerNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String input = playerNameInput.getText().toString();
                    if (input.matches("[a-zA-Z]+")) {
                        playerName = input;
                        Log.i("player", "name saved: " + playerName);
                    }
                    playerNameInput.setText(playerName);
                }
                return false;
            }
        });

        factHelper = new FactHelper();
        fact = findViewById(R.id.textView_fact);
        fact.setText(factHelper.getFact());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Date lastDate = new Date(lastLogin);
        if (dateHelper.isNextDay(lastDate)) {
            daysInRow++;
        } else if (dateHelper.isSameDay(lastDate)) {
            Log.i("date", "Still the same day! Days retained: " + daysInRow);
        } else {
            daysInRow = 0;
        }
        lastLogin = dateHelper.getCurrentDate().getTime();
        SharedPreferences saveSh = getSharedPreferences("playerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = saveSh.edit();
        edit.putString("name", playerName).apply();
        edit.putInt("days", daysInRow).apply();
        edit.putLong("date", lastLogin).apply();
        edit.apply();
        Log.i("date", "days int saved:" + daysInRow);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUI.hide(this);
        SharedPreferences loadSh = getSharedPreferences("playerPref", Context.MODE_PRIVATE);
        playerName = loadSh.getString("name", DEFAULT_NAME);
        Log.i("player", "name loaded: " + playerName);
        if (!playerName.equals(DEFAULT_NAME)) {
            playerNameInput.setText(String.format(getString(R.string.welcome_back), playerName));
        }
        lastLogin = loadSh.getLong("date", dateHelper.getCurrentDate().getTime());
        daysInRow = loadSh.getInt("days", 0);
        sound = loadSh.getBoolean("sound", true);
        Handler mainHandler = new Handler();
        //Wait for sounds to load:
        if (!audioManager.loadingDone()) {
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sound) {
                        MainActivity.audioManager.playSound(Sound.START);
                    }
                }
            }, 2000);
        } else if (sound) {
            MainActivity.audioManager.playSound(Sound.START);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            SystemUI.hide(this);
        }
    }

    public void playClicked(View view) {
        Intent playIntent = new Intent(this, GameActivity.class);
        startActivity(playIntent);
    }

    public void settingsClicked(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivityForResult(settingsIntent, SettingsActivity.SETTINGS_REQUEST);
    }

    public void scoresClicked(View view) {
        Intent scoresIntent = new Intent(this, ScoresActivity.class);
        startActivity(scoresIntent);
    }

}
