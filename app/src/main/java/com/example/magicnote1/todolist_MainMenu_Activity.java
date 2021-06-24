package com.example.magicnote1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magicnote1.model.Task;
import com.example.magicnote1.model.ToDoList;
import com.example.magicnote1.model.reminderReceiver;
import com.example.magicnote1.model.todolist_item_Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import pl.droidsonroids.gif.GifImageView;

//Activity quản lý task
public class todolist_MainMenu_Activity extends Activity {
    private ListView listViewTask;
    private List<Task> listTask;
    private ToDoList toDoList;
    private ImageView emptyView;
    private Handler handler;
    private AlarmManager mAlarm;
    private Intent notificationReceiver;
    private PendingIntent notificationReceiverPending;
    private int themeId = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_main_menu);
        loadingLogo();
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        listTask = new ArrayList<Task>(0);
        toDoList = new ToDoList(this);
        listViewTask = (ListView)findViewById(R.id.lisk_view_task);
        updateListViewTask();
        listViewTask.setOnItemClickListener(listViewListener);
        int loadThemeId = sharedPreferences.getInt("themeid",0);
        Log.d("id at Create"," "+loadThemeId);
        changeTheme(loadThemeId);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("themeid",themeId);
//        editor.apply();
//    }
    @Override
    protected void onResume(){
        super.onResume();
        updateListViewTask();
        int loadThemeId = sharedPreferences.getInt("themeid",0);
        Log.d("id at Resume"," "+loadThemeId);
        changeTheme(loadThemeId);
    }
    //Xử lý khi nhấn vào task
    private AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int taskID = listTask.get(position).getIdTask();
            goToTaskById(taskID);
        }
    };

    //Cập nhật Listview
    private void updateListViewTask(){
        List<String> taskResult = new ArrayList<String>(0);
        listTask = toDoList.getAllTask();
        for (int i = 0; i < listTask.size();i++){
            taskResult.add(listTask.get(i).result());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_layout, R.id.item_view, taskResult){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowItem = getLayoutInflater().inflate(R.layout.item_layout,null);
                CheckBox checkDone = (CheckBox) rowItem.findViewById(R.id.check_done);
                TextView itemView = (TextView)rowItem.findViewById(R.id.item_view);
                itemView.setText(listTask.get(position).result());
                itemView.setPaintFlags(0);
                if(listTask.get(position).getCompleted())
                {
                    checkDone.setChecked(true);
                    rowItem.setBackgroundResource(R.drawable.round_item_completed);
                    itemView.setPaintFlags(itemView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Calendar calendar = Calendar.getInstance();
                        notificationReceiver = new Intent(todolist_MainMenu_Activity.this,
                                reminderReceiver.class);
                        notificationReceiver.putExtra("content alarm",listTask.get(position).getTaskDetails());
                        Log.d("123",listTask.get(position).getTaskDetails());
                        notificationReceiverPending = PendingIntent.getBroadcast(
                                todolist_MainMenu_Activity.this, listTask.get(position).getIdTask(), notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);

                        if(buttonView.isChecked()) {
                            listTask.get(position).setCompleted(true);
                            rowItem.setBackgroundResource(R.drawable.round_item_completed);
                            itemView.setPaintFlags(itemView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            mAlarm.cancel(notificationReceiverPending);
                        } else {
                            Log.d("id noti main"," "+listTask.get(position).getIdTask());
                            listTask.get(position).setCompleted(false);
                            rowItem.setBackgroundResource(R.drawable.round_item);
                            itemView.setPaintFlags(0);
                            if(listTask.get(position).getDate().length()>0) {
                                long wTime = Long.valueOf(listTask.get(position).getDate()) - toMillis(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                                if (wTime > 0) {
                                    setReminder(wTime);
                                } else setReminder(86460000 + wTime);
                            }
                        }
                        toDoList.updateTask(listTask.get(position));
                    }
                });
                return rowItem;
            }
        };
        listViewTask.setAdapter(arrayAdapter);
        checkEmpty(taskResult);
    }
    //Đẩy id của task và mở activity todolist_item_Activity
    protected void goToTaskById(int id){
        Intent intent = new Intent(this, todolist_item_Activity.class);
        intent.putExtra("id",id);
        startActivity(intent);
        overridePendingTransition(R.anim.splash_in_anim, R.anim.splash_out_anim);
    }

    public void clickButton (View v){
        switch (v.getId()) {
            case R.id.button_add_task_form:
                Intent intent = new Intent(this, todolist_item_Activity.class);
                startActivity(intent);
                //Transition animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //
                break;
            case R.id.change_theme_button:
                int loadThemeId = sharedPreferences.getInt("themeid",0);
                Log.d("id at click"," "+loadThemeId);
                loadThemeId++;
                if(loadThemeId>3){
                    loadThemeId = 0;
                }
                changeTheme(loadThemeId);
                Log.d("id after click"," "+loadThemeId);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("themeid",loadThemeId);
                editor.apply();
                break;
        }
    }
    public void checkEmpty(List list){
        emptyView = (ImageView) findViewById(R.id.empty_view);
        if(list.size()>0){
            emptyView.setVisibility(View.GONE);
        }
        else emptyView.setVisibility(View.VISIBLE);
    }
    public void loadingLogo(){
        GifImageView logo =(GifImageView) findViewById(R.id.logo);
//        logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_in_anim));
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(AnimationUtils.loadAnimation(todolist_MainMenu_Activity.this, R.anim.splash_out_anim));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logo.setVisibility(View.GONE);
                    }
                },100);
            }
        },3000);
    }
    private void setReminder(long time){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + time,
                    notificationReceiverPending);
        }
        else mAlarm.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + time,
                notificationReceiverPending);
        String timeHhMm = timeFormat(time);
        long second = Long.valueOf(60-Calendar.getInstance().get(Calendar.SECOND));
        String timeSs ="" + second;
        if(second == 60){
            timeHhMm = timeFormat(time - 60000);
        }
        if(second < 10){
            timeSs = "0"+second;
        }
        Toast.makeText(getApplicationContext(), "Công việc này sẽ được nhắc nhở sau " + timeHhMm  + ":"+ timeSs,
                Toast.LENGTH_LONG).show();
    }
    public long toMillis(int h, int m, int s){
        return Long.valueOf((h*3600 + m *60 + s)*1000);
    }
    public String timeFormat(long millis){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }
    public void changeTheme(int loadThemeId){
        LinearLayout bgView = (LinearLayout)findViewById(R.id.layout_main);
        switch (loadThemeId){
            case 0:
                bgView.setBackgroundResource(R.drawable.bg_todolist1);
                break;
            case 1:
                bgView.setBackgroundResource(R.drawable.bg_todolist2);
                break;
            case 2:
                bgView.setBackgroundResource(R.drawable.bg_todolist3);
                break;
            case 3:
                bgView.setBackgroundResource(R.drawable.bg_todolist4);
                break;
        }
    }
}