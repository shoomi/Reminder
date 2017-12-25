package com.example.andriy.reminder.alarmutils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message

        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtoneSound = RingtoneManager.getRingtone(context.getApplicationContext(), ringtoneUri);
        if (ringtoneSound != null) {
            ringtoneSound.play();
        }
//        try {
//            TimeUnit.SECONDS.sleep(0);       // here you can set the melody playing duration
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                NotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
    }

