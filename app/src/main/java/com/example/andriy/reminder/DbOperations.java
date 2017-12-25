package com.example.andriy.reminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import com.example.andriy.reminder.utils.DBHelper;

import java.util.ArrayList;
import java.util.Map;

import static com.example.andriy.reminder.activities.ReminderActivity.reminderId;
import static com.example.andriy.reminder.activities.MainActivity.remindersList;
import static com.example.andriy.reminder.utils.DBHelper.*;
import static com.example.andriy.reminder.utils.DBHelper.KEY_TEXT;
import static com.example.andriy.reminder.utils.DBHelper.KEY_TIME;

import static com.example.andriy.reminder.utils.DbCreator.dbHelper;

public class DbOperations extends AppCompatActivity {

    public static SQLiteDatabase db;

    public DbOperations() {
        db = dbHelper.getWritableDatabase();
    }

    public void loadDataFromDbToList() {

        remindersList = new ArrayList<Map<String, Object>>();
        Cursor c = null;

        c = db.query(TABLE_CONTACTS, null, null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            do {

                long reminderId = c.getLong(c.getColumnIndexOrThrow(KEY_ID));

                String reminderText = c.getString(c.getColumnIndexOrThrow(KEY_TEXT));
                String reminderTime = c.getString(c.getColumnIndexOrThrow(KEY_TIME));

                new ReminderMap().pushReminderAndTimeAndAddToList(reminderText, reminderTime, reminderId);

            } while (c.moveToNext());
        }
    }

    public long addReminderTextAndTimeToDb(String reminderText, String reminderTime) {

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_TEXT, reminderText);
        cv.put(DBHelper.KEY_TIME, reminderTime);

        return db.insert(DBHelper.TABLE_CONTACTS, null, cv);
    }

    public String getTheRemindersText(Intent intent) {

        Cursor c = null;
        String remindersText = null;

        c = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_TEXT, KEY_STATUS, KEY_TIME}, KEY_ID + "=?", new String[]{String.valueOf(intent.getIntExtra("alarmId", (int) reminderId))}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            do {
                remindersText = c.getString(c.getColumnIndexOrThrow(KEY_TEXT));
            } while (c.moveToNext());
        }
        return remindersText;
    }

    public void deleteItem(String idSelect) {
        db.delete(DBHelper.TABLE_CONTACTS, KEY_ID + "=" + idSelect, null);
    }

    public void deleteAllReminders() {
        db.delete(DBHelper.TABLE_CONTACTS, null, null);
    }
}
