package com.example.andriy.reminder.activities;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;
import com.example.andriy.reminder.*;
import com.example.andriy.reminder.alarmutils.AlarmReceiver;
import com.example.andriy.reminder.utils.CounterClass;

import java.util.Calendar;

import static com.example.andriy.reminder.activities.MainActivity.sAdapter;


public class ReminderActivity extends Activity {

    public static long reminderId;

    private TextView textViewTimeToRemind;
    private TextView remindTimeSetInfo;
    private EditText editTextRemindField;
    private Button btnSetTime;
    private Button btnSetDate;
    private CounterClass countdownTimer;

    private final static int RQS_1 = 1;
    private int daySet;
    private int mouthSet;
    private int yearSet;

    private Calendar calNow = Calendar.getInstance();
    private Calendar calendarToSetTime = (Calendar) calNow.clone();
    private Calendar calendarToSetDate = Calendar.getInstance();

    private static AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private boolean dateIsSet = false;
    private boolean timeIsSet = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminderactivity);

        remindTimeSetInfo = (TextView) findViewById(R.id.tvRemindTimeSetInfo);
        textViewTimeToRemind = (TextView) findViewById(R.id.textViewTimeLeftToRemind);
        editTextRemindField = (EditText) findViewById(R.id.editTextRemindFieldEnter);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);

    }

    public void setTime(View v) {
        remindTimeSetInfo.setText("");
        openTimePickerDialog(true);
    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        if (!dateIsSet) {
            daySet = calNow.get(Calendar.DAY_OF_MONTH);    // get the current day and month if date is not set
            mouthSet = calNow.get(Calendar.MONTH);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(         // get the current time and show it in the timePickerDialog
                ReminderActivity.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarToSetTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarToSetTime.set(Calendar.MINUTE, minute);
            calendarToSetTime.set(Calendar.SECOND, 0);
            calendarToSetTime.set(Calendar.MILLISECOND, 0);
            btnSetTime.setText("time set:  " + hourOfDay + ":" + minute);
            timeIsSet = true;
            if (calendarToSetTime.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calendarToSetTime.add(Calendar.DATE, 1);
            }
        }
    };

    public void setDate(View view) {
        int mYear = calNow.get(Calendar.YEAR);
        int mMonth = calNow.get(Calendar.MONTH);
        int mDay = calNow.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        calendarToSetDate.set(Calendar.MONTH, 1);//month
                        calendarToSetDate.set(Calendar.YEAR, 2016);//year
                        calendarToSetDate.set(Calendar.DAY_OF_MONTH, 12);//day
                        daySet = dayOfMonth;
                        mouthSet = monthOfYear;
                        yearSet = year;
                        btnSetDate.setText("date set:   " + daySet + "." + (mouthSet + 1) + "." + yearSet);
                    }
                }, mYear, mMonth, mDay);
        if (calendarToSetDate.compareTo(calNow) <= 0) {
            // Today Set time passed, count to tomorrow
            calendarToSetDate.add(Calendar.DATE, 1);
        }
        dateIsSet = true;
        datePickerDialog.show();
    }

    public void setTheReminder(View view) {

        if (!editTextRemindField.getText().toString().isEmpty()) {             // set the remind if text is not empty

            if (timeIsSet | dateIsSet) {

                int difDay = (daySet - calendarToSetTime.get(Calendar.DAY_OF_MONTH));
                int difMouth = (mouthSet - calendarToSetTime.get(Calendar.MONTH));
                calendarToSetTime.add(Calendar.DAY_OF_MONTH, difDay);
                calendarToSetTime.add(Calendar.MONDAY, difMouth);
                setTheReminderAndPushItToDbAndList(calendarToSetTime);

            } else Toast.makeText(ReminderActivity.this, "Please, set time", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(ReminderActivity.this, "Please, enter your remind", Toast.LENGTH_LONG).show();
        }

    }

    private void setTheReminderAndPushItToDbAndList(Calendar reminderCalendar) {

        String reminderText = editTextRemindField.getText().toString();

        if (countdownTimer != null) {    //cancel the old countDownTimer
            countdownTimer.cancel();
        }

        int timeLeftToRemind = (int) (reminderCalendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());

        String reminderTime = null;

        if (timeLeftToRemind > 0) {

            countdownTimer = new CounterClass(timeLeftToRemind, textViewTimeToRemind);
            countdownTimer.startCounting();

            reminderTime = (String) DateFormat.format("dd.MM.yyyy   hh:mm", reminderCalendar.getTime());
            remindTimeSetInfo.setText("Set on:           " + reminderTime + "");

            Cursor c = null;

            reminderId = new DbOperations().addReminderTextAndTimeToDb(reminderText, reminderTime);   /// ad reminder to database and return it id

            Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            intent.putExtra("alarmId", (int) reminderId);
            pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, (int) reminderId);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   // set the alarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(),
                    pendingIntent);

            new ReminderMap().pushReminderAndTimeAndAddToList(reminderText, reminderTime, reminderId);

            sAdapter.notifyDataSetChanged();
            setResult(RESULT_OK);

            Toast.makeText(ReminderActivity.this, "Reminder has been set", Toast.LENGTH_LONG).show();

            editTextRemindField.setText("");

        } else {
            Toast.makeText(ReminderActivity.this, "You've entered invalid time", Toast.LENGTH_LONG).show();
        }

        btnSetTime.setText("SET TIME");
        btnSetDate.setText("SET DATE");
        dateIsSet = false;
        timeIsSet = false;

    }


    public void cancelReminder(View view) {

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            remindTimeSetInfo.setText("Reminder was canceled");
            if (countdownTimer != null) {
                countdownTimer.cancel();
            }
        } else
            Toast.makeText(ReminderActivity.this, "Please set the reminder first", Toast.LENGTH_LONG).show();
    }


}
