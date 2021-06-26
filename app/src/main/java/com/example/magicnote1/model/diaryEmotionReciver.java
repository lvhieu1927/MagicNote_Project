package com.example.magicnote1.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.magicnote1.R;
import com.example.magicnote1.todolist_MainMenu_Activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class diaryEmotionReciver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        String CHANNEL_ID = "MESSAGE";
        String CHANNEL_NAME = "MESSAGE";
        sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        Log.d("123", "lang at on create menu " + lang);
        setAppLang(lang,context);
        Intent notiIntent = new Intent(context, todolist_MainMenu_Activity.class);
        Log.d("123",intent.getStringExtra("content_setting"));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notiIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(context.getString(R.string.remiderTodolist))
                .setContentText(context.getString(R.string.remiderTodolist1))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.todolistContent) + intent.getStringExtra("content_setting")))
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 200, 200, 300})
                .build();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        manager.notify(getRandomNumber(), notification);
    }
    private static int getRandomNumber() {
        Date dd= new Date();
        SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
    }
    public void setAppLang(String local,Context context){
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}
