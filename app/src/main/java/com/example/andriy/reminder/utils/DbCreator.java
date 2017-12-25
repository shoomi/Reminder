package com.example.andriy.reminder.utils;

import android.app.Activity;


public class DbCreator {

    public static DBHelper dbHelper;

    public void getDbInstance(Activity activity) {
        dbHelper = new DBHelper(activity);
    }



}
