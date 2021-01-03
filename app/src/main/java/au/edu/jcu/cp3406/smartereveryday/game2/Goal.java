package au.edu.jcu.cp3406.smartereveryday.game2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

class Goal {
    private static final int GOAL_HEIGHT = 100;

    private int boundingWidth, goalWidth;
    private double x, y, xDir, speed;
    private float left, right, bottom;

    Goal(int boundingWidth, int boundingHeight, int goalWidthFactor, int speed) {
        this.goalWidth = boundingWidth / goalWidthFactor;
        this.boundingWidth = boundingWidth - goalWidth;

        Random random = new Random();
        x = random.nextInt(this.boundingWidth);
        y = boundingHeight;

        this.speed = speed;
        xDir = 1;
    }

    void draw(Canvas canvas) {
        canvas.save();

        left = (float) x;
        float top = (float) y - GOAL_HEIGHT;
        right = (float) x + goalWidth;
        bottom = (float) y;

        Paint goalPaint = new Paint();
        goalPaint.setColor(Color.GREEN);
        goalPaint.setShadowLayer(10, 0, 20, Color.BLACK);
        canvas.drawRect(left, top, right, bottom, goalPaint);

        canvas.restore();
    }

    void move() {
        x += xDir * speed;

        if ((x < 1 && xDir < 0) || (x >= boundingWidth && xDir > 0)) {
            xDir *= -1;
        }

    }

    float getLeft() {
        return left;
    }

    float getRight() {
        return right;
    }

    float getBottom() {
        return bottom;
    }

}

