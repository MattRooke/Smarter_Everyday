package au.edu.jcu.cp3406.smartereveryday.utils;

import android.util.Log;

import java.util.Locale;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Tweet {
    private Twitter twitter = TwitterFactory.getSingleton();

    public Tweet(int score, String game, String difficulty) {
        final String string = String.format(Locale.getDefault(), "Just scored %d points in %s on %s in the new #SmarterEveryday app!", score, game, difficulty);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    twitter.updateStatus(string);
                    Log.i("twitter", twitter.getOAuthAccessToken().toString());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
