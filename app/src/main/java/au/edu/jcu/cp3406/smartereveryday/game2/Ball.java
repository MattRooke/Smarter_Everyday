package au.edu.jcu.cp3406.smartereveryday.game2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

class Ball {
    private static final int BALL_RADIUS = 50;
    int boundingHeight;
    private int boundingWidth;
    private double x, y, xDir, yDir, speed;

    Ball(int boundingWidth, int boundingHeight, int speed) {
        this.boundingWidth = boundingWidth - BALL_RADIUS;
        this.boundingHeight = boundingHeight - BALL_RADIUS;

        Random random = new Random();

        x = random.nextInt(this.boundingWidth);
        y = BALL_RADIUS;
        this.speed = speed;
        xDir = 0;
        yDir = 1;
    }

    void draw(Canvas canvas) {
        canvas.save();

        float top = (float) y;
        float left = (float) x;

        Paint ballPaint = new Paint();
        ballPaint.setColor(Color.GRAY);
        ballPaint.setShadowLayer(10, 0, 20, Color.BLACK);
        canvas.drawCircle(left, top, BALL_RADIUS, ballPaint);

        canvas.restore();
    }

    void move(double sensorX) {
        sensorX *= .7; // Tweak input sensitivity.
        xDir = -sensorX;
        x += xDir * speed;
        y += yDir * speed;

        if (x >= boundingWidth && xDir > 0) {
            x = boundingWidth;
        } else if (x < BALL_RADIUS && xDir < 0) {
            x = BALL_RADIUS;
        }

        if ((y < 1 && yDir < 0) || (y >= boundingHeight && yDir > 0)) {
            yDir = 0;
            y = boundingHeight;
        }
    }

    double getX() {
        return x;
    }

    double getY() {
        return y + BALL_RADIUS;
    }

    void scored() {
        xDir = 0;
        yDir = 0;
        x = 0;
        y = -100; //hide ball off screen
    }
}

