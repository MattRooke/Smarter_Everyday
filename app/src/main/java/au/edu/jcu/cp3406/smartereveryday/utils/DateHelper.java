package au.edu.jcu.cp3406.smartereveryday.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final String LOG_TAG = "DateHelper";
    private Date current;

    public DateHelper() {
        this.current = getCurrentDate();
    }

    public Date getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        Log.i(LOG_TAG, "Current time: " + date);
        return date;
    }

    int getCurrentDay() {
        Date date = getCurrentDate();
        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = df.format(date);
        return Integer.parseInt(formattedDate);
    }

    public int getDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = df.format(date);
        return Integer.parseInt(formattedDate);
    }

    String getFormattedDate() {
        Date date = getCurrentDate();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(date);
        Log.i(LOG_TAG, "Formatted Date: " + formattedDate);
        return formattedDate;
    }

    public boolean isNextDay(Date last) {
        long daysInMilli = 86400000;
        float difference = current.getTime() - last.getTime();
        float elapsedDays = difference / daysInMilli;
        return elapsedDays >= 1 && elapsedDays < 2;
    }

    public boolean isSameDay(Date last) {
        long daysInMilli = 86400000;
        float difference = current.getTime() - last.getTime();
        float elapsedDays = difference / daysInMilli;
        return elapsedDays > 0 && elapsedDays < 1;
    }

}
