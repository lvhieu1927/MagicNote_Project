package com.example.magicnote1.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.SoundEffectConstants;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.magicnote1.R;
import com.example.magicnote1.model.todolist_item_Activity;
import com.example.magicnote1.todolist_MainMenu_Activity;

import java.text.SimpleDateFormat;
import java.util.Date;
public class reminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            String CHANNEL_ID = "MESSAGE";
            String CHANNEL_NAME = "MESSAGE";
            Intent notiIntent = new Intent(context, todolist_MainMenu_Activity.class);
            Log.d("123",intent.getStringExtra("content alarm"));
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
                    .setContentTitle("Nhắc nhở công việc")  
                    .setContentText("Bạn có công việc cần làm bây giờ. Check ngay !")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Nội dung: " + intent.getStringExtra("content alarm")))
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 200, 200, 300})
                    .build();
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
            manager.notify(getRandomNumber(), notification);
            Log.d("12345","đã đến đây");
    }
    private static int getRandomNumber() {
        Date dd= new Date();
        SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
    }
}