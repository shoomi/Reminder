package com.example.andriy.reminder.alarmutils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.andriy.reminder.DbOperations;
import com.example.andriy.reminder.R;
import com.example.andriy.reminder.activities.ReminderActivity;

import static com.example.andriy.reminder.activities.ReminderActivity.reminderId;

public class AlarmService extends IntentService {


    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        String name;
        name = new DbOperations().getTheRemindersText(intent);  // get the reminder text from db depending on time
        sendNotification(name);
    }


    private void sendNotification(String msg) {

        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) reminderId,
                new Intent(this, ReminderActivity.class), 0);

        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this)
                .setContentTitle(msg).setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(msg);
        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
    }


}