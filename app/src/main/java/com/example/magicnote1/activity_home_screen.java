package com.example.magicnote1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.magicnote1.chart.PieChartActivity;
import com.example.magicnote1.model.Task;
import com.example.magicnote1.model.diaryEmotionReciver;
import com.example.magicnote1.model.reminderReceiver;
import com.example.magicnote1.model.todolist_item_Activity;
import com.example.magicnote1.splash.SplashScreenWishlist;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class activity_home_screen extends Activity {

    private CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;
    private SharedPreferences sharedPreferences;
    private Intent notificationReceiver;
    private PendingIntent notificationReceiverPending;
    private AlarmManager mAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        addControl();
        addEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean todolistOpt1 = sharedPreferences.getBoolean("todolistOpt1",false);
        boolean todolistOpt2 = sharedPreferences.getBoolean("todolistOpt2",false);
        checkLoadSetting(todolistOpt1,todolistOpt2,getTimeOpt1(),getTimeOpt2());
    }

    private void addEvent() {
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this,todolist_MainMenu_Activity.class);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this,MoodDiaryMainMenu.class);
                startActivityForResult(intent,2);
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this,todolist_MainMenu_Activity.class);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this, SplashScreenWishlist.class);
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this, PieChartActivity.class);
                startActivity(intent);
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this, setting_Activity.class);
                startActivity(intent);
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this, aboutApp_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        cardView1 = findViewById(R.id.card1);
        cardView2 = findViewById(R.id.card2);
        cardView3 = findViewById(R.id.card3);
        cardView4 = findViewById(R.id.card4);
        cardView5 = findViewById(R.id.card5);
        cardView6 = findViewById(R.id.card6);
    }
    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
    private void setReminder(long time,int id) {
        notificationReceiver = new Intent(activity_home_screen.this,
                diaryEmotionReciver.class);
        if(id == -888) {
            notificationReceiver.putExtra("content_setting", getString(R.string.settingTodolist));
        } else if(id == -999){
            notificationReceiver.putExtra("content_setting", getString(R.string.settingEmotion));
        }
        notificationReceiverPending = PendingIntent.getBroadcast(
                activity_home_screen.this, id, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + time,
                    notificationReceiverPending);
        } else {
            mAlarm.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + time,
                    notificationReceiverPending);
        }
    }
    public void checkLoadSetting(boolean a, boolean b, long t1, long t2){
        if(a){
            if(t1>=0) {
                setReminder(t1, -999);
//                Toast.makeText(getApplicationContext(), " " + timeFormat(t1), Toast.LENGTH_LONG).show();
            } else {
                setReminder(86400000 + t1, -999);
//                Toast.makeText(getApplicationContext(), " " + timeFormat(86400000 + t1), Toast.LENGTH_LONG).show();
            }
        } else{
            cancelReminder(-999);
//            Toast.makeText(getApplicationContext(), "Da huy toi 6h sang", Toast.LENGTH_LONG).show();
        }
        if (b){
            if (t2>=0) {
                setReminder(t2, -888);
//                Toast.makeText(getApplicationContext(), " " + timeFormat(t2), Toast.LENGTH_LONG).show();
            } else {
                setReminder(86400000 + t2,-888);
//                Toast.makeText(getApplicationContext(), " " + timeFormat(86400000 + t2), Toast.LENGTH_LONG).show();
            }
        } else {
            cancelReminder(-888);
//            Toast.makeText(getApplicationContext(), "Da set toi 8h toi", Toast.LENGTH_LONG).show();
        }
    }
    private void cancelReminder(int id){
        notificationReceiver = new Intent(activity_home_screen.this,
                diaryEmotionReciver.class);
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationReceiverPending = PendingIntent.getBroadcast(
                activity_home_screen.this, id, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarm.cancel(notificationReceiverPending);
    }
    public long getTimeOpt2(){
        long time;
        Calendar calendar = Calendar.getInstance();
        time = toMillis(20,0,0) - toMillis(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        return time;
    }
    public long getTimeOpt1(){
        long time;
        Calendar calendar = Calendar.getInstance();
        time = toMillis(6,0,0) - toMillis(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        return time;
    }
    public long toMillis(int h, int m, int s){
        return Long.valueOf((h*3600 + m *60 + s)*1000);
    }
//    public String timeFormat(long millis){
//        String t = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
//                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
//        return t;
//    }
}