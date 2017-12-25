package com.example.andriy.reminder.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;


public class CounterClass {

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;
    private TextView textViewTime;

    public CounterClass(long mTimeLeftInMillis, TextView textViewTime) {
        this.mTimeLeftInMillis = mTimeLeftInMillis;
        this.textViewTime = textViewTime;
    }


    public void startCounting() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                textViewTime.setText("Completed.");
            }
        }.start();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewTime.setText(timeLeftFormatted);
    }

    public void cancel() {
        mCountDownTimer.cancel();
    }


}

