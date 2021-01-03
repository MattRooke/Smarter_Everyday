package au.edu.jcu.cp3406.smartereveryday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import au.edu.jcu.cp3406.smartereveryday.utils.SystemUI;

public class SettingsActivity extends AppCompatActivity {
    static int SETTINGS_REQUEST = 1;
    Spinner difficultySpin;
    Switch soundSwitch, onlineSwitch;
    int difficulty; // 0 - "easy", 1 - "medium", 2 - "hard", 3 - "expert"
    boolean sound, online; // True - "on", False - "off"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SystemUI.hide(this);

        difficultySpin = findViewById(R.id.spinner_difficulty);
        soundSwitch = findViewById(R.id.switch_sound);
        onlineSwitch = findViewById(R.id.switch_online);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUI.hide(this);
        SharedPreferences loadSh = getSharedPreferences("playerPref", Context.MODE_PRIVATE);
        difficulty = loadSh.getInt("difficulty", 0);
        sound = loadSh.getBoolean("sound", true);
        online = loadSh.getBoolean("online", true);
        difficultySpin.setSelection(difficulty);
        soundSwitch.setChecked(sound);
        onlineSwitch.setChecked(online);
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

    public void saveClicked(View view) {
        Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show();
        difficulty = difficultySpin.getSelectedItemPosition();
        sound = soundSwitch.isChecked();
        online = onlineSwitch.isChecked();
        SharedPreferences saveSh = getSharedPreferences("playerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = saveSh.edit();
        edit.putInt("difficulty", difficulty).apply();
        edit.putBoolean("sound", sound).apply();
        edit.putBoolean("online", online).apply();
        edit.apply();
        finish();
    }

    public void backClicked(View view) {
        Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
