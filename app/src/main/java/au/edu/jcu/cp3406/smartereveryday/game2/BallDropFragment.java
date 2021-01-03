package au.edu.jcu.cp3406.smartereveryday.game2;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import au.edu.jcu.cp3406.smartereveryday.GameActivity;
import au.edu.jcu.cp3406.smartereveryday.MainActivity;
import au.edu.jcu.cp3406.smartereveryday.R;
import au.edu.jcu.cp3406.smartereveryday.audio.Sound;
import au.edu.jcu.cp3406.smartereveryday.utils.DatabaseHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.Difficulty;
import au.edu.jcu.cp3406.smartereveryday.utils.SystemUI;
import au.edu.jcu.cp3406.smartereveryday.utils.Tweet;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BallDropFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BallDropFragment extends Fragment implements View.OnClickListener, SensorEventListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GAME_OVER_DELAY = 2500;

    private int width, height, speed, scoreModifier, goalWidthFactor, score, ballsLeft;
    private double x, y;
    private String strDifficulty, name;
    private Handler mainHandler;
    private SensorManager sensorManager;
    private Sensor mRotation;
    private Runnable redraw;
    private BallDropView ballDropView;
    private Ball ball;
    private List<Goal> goals;
    private boolean isRedrawing;
    private Difficulty difficulty;
    private TextView scoreView, ballsLeftView;
    private DatabaseHelper databaseHelper;

    public BallDropFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param difficulty Parameter 1.
     * @param name       Parameter 2
     * @return A new instance of fragment ball_maze.
     */
    public static BallDropFragment newInstance(String difficulty, String name) {
        BallDropFragment fragment = new BallDropFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, difficulty);
        args.putString(ARG_PARAM2, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            strDifficulty = getArguments().getString(ARG_PARAM1);
            assert strDifficulty != null;
            difficulty = Difficulty.valueOf(strDifficulty.toUpperCase());
            name = getArguments().getString(ARG_PARAM2);
        }
        this.sensorManager = (SensorManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : deviceSensors) {
            Log.i("Sensors", sensor.getName());
        }
        mRotation = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //set variables based on difficulty:
        switch (difficulty) {
            case EASY:
                speed = 10;
                scoreModifier = 50;
                goalWidthFactor = 3;
                ballsLeft = 7;
                break;
            case MEDIUM:
                speed = 15;
                scoreModifier = 100;
                goalWidthFactor = 5;
                ballsLeft = 6;
                break;
            case HARD:
                speed = 20;
                scoreModifier = 200;
                goalWidthFactor = 5;
                ballsLeft = 5;
                break;
            case EXPERT:
                speed = 25;
                scoreModifier = 400;
                goalWidthFactor = 6;
                ballsLeft = 4;
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ballsLeft", ballsLeft);
        outState.putInt("score", score);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ball_drop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SystemUI.hide(this);
        ballDropView = Objects.requireNonNull(getView()).findViewById(R.id.mazeView);

        scoreView = Objects.requireNonNull(getView()).findViewById(R.id.textView_score2);
        scoreView.setText(String.format(getString(R.string.score_displayer), score));

        ballsLeftView = Objects.requireNonNull(getView()).findViewById(R.id.textView_balls_remaining);
        ballsLeftView.setText(String.format(getString(R.string.ball_displayer), ballsLeft));

        final Button buttonDrop = Objects.requireNonNull(getView()).findViewById(R.id.button_drop);
        buttonDrop.setOnClickListener(this);

        ImageButton buttonQuit = Objects.requireNonNull(getView()).findViewById(R.id.button_quit2);
        buttonQuit.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score");
            ballsLeft = savedInstanceState.getInt("ballsLeft");
        }

        mainHandler = new Handler();
        redraw = new Runnable() {
            @Override
            public void run() {
                if (isRedrawing) {
                    if (ballsLeft >= 0) {
                        move();
                        ballDropView.invalidate();
                        ballsLeftView.setText(String.format(getString(R.string.ball_displayer), ballsLeft));
                        mainHandler.postDelayed(redraw, 16);
                        //when the last ball reaches the bottom of the screen:
                        if (ballsLeft == 0 && ball != null) {
                            if (ball.getY() >= ball.boundingHeight)
                                buttonDrop.setText(R.string.finish_game);
                        }
                    } else {
                        ballDropView.setVisibility(View.GONE);
                        ballsLeftView.setText(getString(R.string.game_over));
                        mainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                endGame();
                            }
                        }, GAME_OVER_DELAY);
                    }
                }
            }
        };
    }

    private void move() {
        if (ball != null) {
            switch (Objects.requireNonNull(getActivity()).getResources().getConfiguration().orientation) {
                case (Configuration.ORIENTATION_PORTRAIT):
                    ball.move(x);
                    break;
                case (Configuration.ORIENTATION_LANDSCAPE):
                    ball.move(-y);
                    break;
            }

        }
        if (goals != null) {
            for (Goal goal : goals
            ) {
                goal.move();
                //if the ball lands in the goal:
                if (ball.getX() < goal.getRight() && ball.getX() > goal.getLeft() && ball.getY() >= goal.getBottom()) {
                    ball.scored();
                    score += scoreModifier;
                    scoreView.setText(String.format(getString(R.string.score_displayer), score));
                    if (GameActivity.sound) {
                        MainActivity.audioManager.playSound(Sound.WIN);
                    }
                } else if (ball.getY() == height) {
                    ball = new Ball(width, height, 0);
                    if (GameActivity.sound) {
                        MainActivity.audioManager.playSound(Sound.FAIL);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mainHandler.post(redraw);
        isRedrawing = true;
        sensorManager.registerListener(this, mRotation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        isRedrawing = false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_quit2:
                quitGame();
                break;
            case R.id.button_drop:
                ballsLeft -= 1;
                width = ballDropView.getWidth();
                height = ballDropView.getHeight();
                ball = new Ball(width, height, speed);
                goals = new ArrayList<>();
                goals.add(new Goal(width, height, goalWidthFactor, speed));
                ballDropView.setGoals(goals);
                ballDropView.setBall(ball);
                break;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x, y, z;
        x = sensorEvent.values[0];
        this.x = x;
        y = sensorEvent.values[1];
        this.y = y;
        z = sensorEvent.values[2];
        Log.i("Sensors", "x " + x + " y " + y + " z " + z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void quitGame() {
        Toast.makeText(getActivity(), "Game Quit!", Toast.LENGTH_SHORT).show();
        FragmentTransaction fragmentTransaction = GameActivity.fragmentManager.beginTransaction();
        fragmentTransaction.remove(this).commit();
    }

    private void endGame() {
        FragmentTransaction fragmentTransaction = GameActivity.fragmentManager.beginTransaction();
        fragmentTransaction.remove(this).commit();
        //save and tweet score:
        if (score > 0) {
            databaseHelper.insertScore(databaseHelper.getWritableDatabase(), "GAME2", name, score);
            if (GameActivity.online) {
                new Tweet(score, "Ball Drop", strDifficulty);
                Toast.makeText(getActivity(), "High Score Shared!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
