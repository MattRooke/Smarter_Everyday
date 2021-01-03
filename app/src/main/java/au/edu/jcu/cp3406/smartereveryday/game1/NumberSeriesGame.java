package au.edu.jcu.cp3406.smartereveryday.game1;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Random;

import au.edu.jcu.cp3406.smartereveryday.GameActivity;
import au.edu.jcu.cp3406.smartereveryday.MainActivity;
import au.edu.jcu.cp3406.smartereveryday.R;
import au.edu.jcu.cp3406.smartereveryday.audio.Sound;
import au.edu.jcu.cp3406.smartereveryday.utils.DatabaseHelper;
import au.edu.jcu.cp3406.smartereveryday.utils.Difficulty;
import au.edu.jcu.cp3406.smartereveryday.utils.Tweet;

class NumberSeriesGame {
    private static final int GAME_OVER_DELAY = 2500;

    private int maxNumbers, scoreModifier, delay;
    private int strikes = 0;
    private int score = 0;
    private String name, strDifficulty;
    private String numberSeries = "";
    private TextView instruction, numberView;
    private Handler mainHandler;
    private Fragment fragment;
    private Random random = new Random();
    private EditText guess;
    private ImageView[] strikeViews;
    private Resources res;
    private DatabaseHelper databaseHelper;
    private Context context;

    NumberSeriesGame(Difficulty difficulty, Fragment fragment, EditText guess,
                     TextView instruction, TextView numberView,
                     ImageView[] strikeViews, Context context, String name) {
        this.fragment = fragment;
        this.guess = guess;
        this.instruction = instruction;
        this.numberView = numberView;
        this.strikeViews = strikeViews;
        res = fragment.getResources();
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        this.name = name;
        strDifficulty = difficulty.toString();

        switch (difficulty) {
            case EASY:
                delay = 4000;
                scoreModifier = 50;
                maxNumbers = 5;
                break;
            case MEDIUM:
                delay = 3000;
                scoreModifier = 100;
                maxNumbers = 8;
                break;
            case HARD:
                delay = 2000;
                scoreModifier = 200;
                maxNumbers = 14;
                break;
            case EXPERT:
                delay = 1000;
                scoreModifier = 300;
                maxNumbers = 17;
                break;
        }
    }

    void runGame() {
        String inputGuess = this.guess.getText().toString();
        Log.i("game", "guess: " + inputGuess);
        this.guess.setText("");
        Log.i("game", "guess: " + inputGuess);
        if (numberSeries.length() == maxNumbers) {
            guess.setVisibility(View.GONE);
            instruction.setText(R.string.you_won);
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endGame();
                }
            }, GAME_OVER_DELAY);
        } else if (!inputGuess.equals(numberSeries)) {
            strikeViews[strikes].setVisibility(View.VISIBLE);
            strikes++;
            if (strikes == 3) {
                guess.setVisibility(View.GONE);
                this.instruction.setText(R.string.game_over);
                this.numberView.setText(String.format(res.getString(R.string.the_number_was), numberSeries));
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        endGame();
                    }
                }, GAME_OVER_DELAY);
            } else {
                this.instruction.setText(R.string.wrong);
                if (GameActivity.sound) {
                    MainActivity.audioManager.playSound(Sound.FAIL);
                }
            }
        } else {
            this.instruction.setText(R.string.correct);
            score += scoreModifier * numberSeries.length();
            if (GameActivity.sound) {
                MainActivity.audioManager.playSound(Sound.WIN);
            }
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextNumber();
                }
            }, delay);
        }
    }

    void nextNumber() {
        numberSeries += Integer.toString(random.nextInt(9));
        numberView.setText(numberSeries);
        this.instruction.setText(R.string.remember);
        guess.setVisibility(View.GONE);
        question();
    }

    void resumeGame() {
        numberView.setText(numberSeries);
        guess.setVisibility(View.GONE);
        for (int i = 0; i < strikes; i++) {
            strikeViews[i].setVisibility(View.VISIBLE);
        }
        question();
    }

    private void question() {
        mainHandler = new Handler();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                numberView.setText("");
                instruction.setText(R.string.what_was_number);
                guess.setVisibility(View.VISIBLE);
                Log.i("game", "series set to " + numberSeries);
            }
        }, delay);
    }

    private void endGame() {
        FragmentTransaction fragmentTransaction = GameActivity.fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
        //save and tweet score:
        if (score > 0) {
            databaseHelper.insertScore(databaseHelper.getWritableDatabase(), "GAME1", name, score);
            if (GameActivity.online) {
                new Tweet(score, "Memory Builder", strDifficulty);
                Toast.makeText(context, "High Score Shared!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String getNumberSeries() {
        return numberSeries;
    }

    void setNumberSeries(String numberSeries) {
        this.numberSeries = numberSeries;
    }

    int getStrikes() {
        return strikes;
    }

    void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }
}
