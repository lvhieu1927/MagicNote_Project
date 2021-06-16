package com.example.magicnote1.todolist;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.example.magicnote1.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class reminderReceiver extends BroadcastReceiver {

    // Notification Sound and Vibration on Arrival
    private Uri soundURI = Uri
            .parse("android.resource://com.example.magicnote1.todolist/"
                    + R.raw.alarm_rooster);
    private long[] mVibratePattern = { 0, 200, 200, 300 };

    @Override
    public void onReceive(Context context, Intent intent) {

            String CHANNEL_ID = "MESSAGE";
            String CHANNEL_NAME = "MESSAGE";

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.bamen_icon_16)
                    .setContentTitle("test")
                    .setContentText("test")
                    .build();
            manager.notify(getRandomNumber(), notification);

    }
    private static int getRandomNumber() {
        Date dd= new Date();
        SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
    }
}