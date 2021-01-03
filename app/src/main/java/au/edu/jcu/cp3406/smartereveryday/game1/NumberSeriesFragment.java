package au.edu.jcu.cp3406.smartereveryday.game1;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import au.edu.jcu.cp3406.smartereveryday.utils.Difficulty;
import au.edu.jcu.cp3406.smartereveryday.GameActivity;
import au.edu.jcu.cp3406.smartereveryday.R;
import au.edu.jcu.cp3406.smartereveryday.utils.SystemUI;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumberSeriesFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView scoreView;

    private String name;
    private Difficulty difficulty;
    private NumberSeriesGame currentGame;

    public NumberSeriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param difficulty Parameter 1.
     * @param name Parameter 2.
     * @return A new instance of fragment Number Series.
     */
    public static NumberSeriesFragment newInstance(String difficulty, String name) {
        NumberSeriesFragment fragment = new NumberSeriesFragment();
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
            String strDifficulty = getArguments().getString(ARG_PARAM1);
            assert strDifficulty != null;
            difficulty = Difficulty.valueOf(strDifficulty.toUpperCase());
            name = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberSeries", currentGame.getNumberSeries());
        outState.putInt("strikes", currentGame.getStrikes());
        outState.putInt("score", currentGame.getScore());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SystemUI.hide(this);
        return inflater.inflate(R.layout.fragment_number_series, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SystemUI.hide(this);

        scoreView = Objects.requireNonNull(getView()).findViewById(R.id.textView_score1);
        TextView numberView = Objects.requireNonNull(getView()).findViewById(R.id.textView_number);
        TextView instruction = Objects.requireNonNull(getView()).findViewById(R.id.textView_instruction);
        EditText guess = Objects.requireNonNull(getView()).findViewById(R.id.editText_guess);
        ImageView[] strikeViews = new ImageView[]{
                Objects.requireNonNull(getView()).findViewById(R.id.imageView_strike1),
                Objects.requireNonNull(getView()).findViewById(R.id.imageView_strike2),
                Objects.requireNonNull(getView()).findViewById(R.id.imageView_strike3)
        };

        currentGame = new NumberSeriesGame(difficulty, this, guess, instruction, numberView, strikeViews, getContext(), name);

        if (savedInstanceState != null) {
            currentGame.setNumberSeries(savedInstanceState.getString("numberSeries"));
            currentGame.setStrikes(savedInstanceState.getInt("strikes"));
            currentGame.setScore(savedInstanceState.getInt("score"));
            setScoreView(String.format(getString(R.string.score_displayer), currentGame.getScore()));
            currentGame.resumeGame();
        } else {
            currentGame.nextNumber();
        }

        guess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    currentGame.runGame();
                    setScoreView(String.format(getString(R.string.score_displayer), currentGame.getScore()));
                }
                return false;
            }
        });

        ImageButton buttonQuit = Objects.requireNonNull(getView()).findViewById(R.id.button_quit1);
        buttonQuit.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentGame.resumeGame();
        SystemUI.hide(this);
    }

    private void setScoreView(String score) {
        scoreView.setText(score);
    }

    @Override
    public void onClick(View view) {
        quitGame();
    }

    private void quitGame() {
        Toast.makeText(getActivity(), "Game Quit!", Toast.LENGTH_SHORT).show();
        FragmentTransaction fragmentTransaction = GameActivity.fragmentManager.beginTransaction();
        fragmentTransaction.remove(this).commit();
    }
}
