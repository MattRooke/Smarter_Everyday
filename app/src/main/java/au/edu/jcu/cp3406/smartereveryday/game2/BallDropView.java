package au.edu.jcu.cp3406.smartereveryday.game2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class BallDropView extends View {
    private Ball ball;
    private List<Goal> goals;

    public BallDropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBall(canvas);

        drawGoals(canvas);
    }

    private void drawGoals(Canvas canvas) {
        if (goals != null) {
            for (Goal goal : goals
            ) {
                goal.draw(canvas);
            }
        }
    }

    private void drawBall(Canvas canvas) {
        if (ball != null) {
            ball.draw(canvas);
        }
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }
}