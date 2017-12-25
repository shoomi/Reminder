package com.example.andriy.reminder;

import java.util.HashMap;
import java.util.Map;

import static com.example.andriy.reminder.activities.MainActivity.remindersList;
import static com.example.andriy.reminder.utils.DBHelper.KEY_TEXT;
import static com.example.andriy.reminder.utils.DBHelper.KEY_TIME;

public class ReminderMap {

    public static Map<String, Object> reminderMap;

   public ReminderMap() {
        reminderMap = new HashMap<String, Object>();
    }

    public void pushReminderAndTimeAndAddToList(String reminderText, String reminderDate, long id) {

        reminderMap.put(KEY_TEXT, reminderText);
        reminderMap.put(KEY_TIME, reminderDate);

        reminderMap.put("row id", Integer.toString((int) id));
        remindersList.add(reminderMap);
    }

}
