package com.example.magicnote1.model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.magicnote1.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//Activity thao tác với task
public class todolist_item_Activity extends Activity {
    private ToDoList toDoList;
    private AlarmManager mAlarm;
    private Intent notificationReceiver;
    private PendingIntent notificationReceiverPending;
    TimePickerDialog timePicker;
    DatePickerDialog datePicker;
    ImageButton clrtime;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        Log.d("123", "lang at on create menu " + lang);
        setAppLang(lang);
        setContentView(R.layout.activity_todolist_item);
        int loadThemeId = sharedPreferences.getInt("themeid",0);
        Log.d("id at Create"," "+loadThemeId);
        changeTheme(loadThemeId);
        toDoList = new ToDoList(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        Button buttonAdd = (Button)findViewById(R.id.button_add_task);
        Button buttonEdit = (Button)findViewById(R.id.button_update_task);
        Button buttonDelete = (Button)findViewById(R.id.button_delete_task);
        if(id != 0){
            buttonAdd.setVisibility(View.GONE);
            loadTaskData(id);
        }
        else{
            buttonAdd.setVisibility(View.VISIBLE);
            buttonEdit.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }
        TextView saveDate = (TextView) findViewById(R.id.due_date);
        TextView showDate = (TextView) findViewById(R.id.showDate);
        clrtime = (ImageButton)findViewById(R.id.clrtime);
        if(saveDate.getText().length()<=0){
            clrtime.setVisibility(View.GONE);
        }
        ImageButton pickTime = (ImageButton)findViewById(R.id.timepicker);
        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                timePicker = new TimePickerDialog(todolist_item_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //long time = toMillis(hourOfDay,minute,0) - toMillis(hour,minutes,second);
                        long time = toMillis(hourOfDay,minute,0);
                        saveDate.setText(String.valueOf(time));
                        showDate.setText(timeFormat(time));
                        clrtime.setVisibility(View.VISIBLE);
                    }
                },hour,minutes, true);
                timePicker.show();
            }
        });
        checkCondRepeat();

    }
    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data){
        super.onActivityResult(requestcode,requestcode,data);
        EditText des = (EditText)findViewById(R.id.task_description);
        if(requestcode == 1){
            if(resultcode == RESULT_OK && data != null){
                ArrayList<String> stt = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                des.setText(stt.get(0));
            }
        }
    }
    public void clickButton (View v){
        switch (v.getId()) {
            case R.id.button_add_task:
                addTask();
                break;
            case R.id.button_update_task:
                updateTask();
                break;
            case R.id.button_delete_task:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Bạn có chắc muốn xoá ?")
                        .setMessage("Khi đã xoá sẽ không thể phục hồi")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTask();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                break;
            case R.id.button_back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.button_speak:
                Intent intentStt = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentStt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
                try {
                    startActivityForResult(intentStt,1);
                }
                catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Oops",Toast.LENGTH_SHORT).show();
                }
            case R.id.clrtime:
                clearTime();
        }
    }
    //Load data của task
    private void loadTaskData(int id){
        TextView textId = (TextView)findViewById(R.id.id_view);
        CheckBox checkPriority = (CheckBox)findViewById(R.id.CBpriority);
        TextView dueDate = (TextView) findViewById(R.id.due_date);
        TextView showDate = (TextView)findViewById(R.id.showDate);
        EditText description = (EditText)findViewById(R.id.task_description);
        CheckBox checkCompleted = (CheckBox)findViewById(R.id.CBcompleted);
        Task task = toDoList.getTask(id);
        textId.setText(String.valueOf(task.getIdTask()));
        checkPriority.setChecked(task.getPriority());
        dueDate.setText(task.getDate());
        setDateChecked(task.getRepeat());
        Log.d("load repeat checked",task.getRepeat());
        if(task.getDate().length()>0) {
            showDate.setText(timeFormat(Long.valueOf(task.getDate())));
        } else showDate.setText("");
        description.setText(task.getTaskDetails());
        checkCompleted.setChecked(task.getCompleted());
    }
    //Thêm task
    private void addTask(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        TextView textId = (TextView)findViewById(R.id.id_view);
        CheckBox checkPriority = (CheckBox)findViewById(R.id.CBpriority);
        TextView dueDate = (TextView) findViewById(R.id.due_date);
        EditText description = (EditText)findViewById(R.id.task_description);
        CheckBox checkCompleted = (CheckBox)findViewById(R.id.CBcompleted);
        Task task = new Task();
        task.setPriority(checkPriority.isChecked());
        task.setDate(dueDate.getText().toString());
        task.setRepeat(getDateChecked());
        Log.d("check repeat",getDateChecked());
        task.setTaskDetails(description.getText().toString());
        task.setCompleted(checkCompleted.isChecked());
        Task taskNew = toDoList.createTask(task);
        textId.setText(String.valueOf(taskNew.getIdTask()));
        //Kiểm tra có nội dung hay không, nếu không có thì xoá
        if(task.getTaskDetails().length() == 0){
            Toast.makeText(getApplicationContext(),"Bạn cần nhập vào nội dung để có thể thêm",Toast.LENGTH_SHORT).show();
            deleteTask();
            Toast.makeText(getApplicationContext(),"Đã xoá task trống",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            if (dueDate.getText().toString().length() > 0) {
                if (!checkCompleted.isChecked()) {
                    if(task.getRepeat().equals("0000000")){
                        cancelScheduleAlarm(taskNew.getIdTask());
                        Log.d("cancel repeat", " "+taskNew.getIdTask() + taskNew.getRepeat());
                    }
                    long wTime = Long.valueOf(dueDate.getText().toString()) - toMillis(hour, minutes, second);
                    if (wTime > 0) {
                        setReminder(wTime, taskNew.getIdTask());
                        setRepeatAlarm(task.getRepeat(), wTime, taskNew.getIdTask());
                        Log.d("task id", "update id " + taskNew.getIdTask());
                    } else {
                        setReminder(86400000 + wTime, taskNew.getIdTask());
                        setRepeatAlarm(task.getRepeat(),AlarmManager.INTERVAL_DAY*7 + wTime, taskNew.getIdTask());
                    }
                }
            }
        }
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //Cập nhật task
    private void updateTask(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        TextView textId = (TextView)findViewById(R.id.id_view);
        CheckBox checkPriority = (CheckBox)findViewById(R.id.CBpriority);
        TextView dueDate = (TextView) findViewById(R.id.due_date);
        EditText description = (EditText)findViewById(R.id.task_description);
        CheckBox checkCompleted = (CheckBox)findViewById(R.id.CBcompleted);
        if(textId.getText().toString().length()>0){
            int id = Integer.valueOf(textId.getText().toString());
            Task task = toDoList.getTask(id);
            task.setPriority(checkPriority.isChecked());
            task.setDate(dueDate.getText().toString());
            task.setRepeat(getDateChecked());
            task.setTaskDetails(description.getText().toString());
            task.setCompleted(checkCompleted.isChecked());
            toDoList.updateTask(task);
            textId.setText(String.valueOf(task.getIdTask()));
            //Kiểm tra có nội dung hay không, nếu không có thì xoá
            if(task.getTaskDetails().length() == 0){
                Toast.makeText(getApplicationContext(),"Bạn cần có nội dung task",Toast.LENGTH_SHORT).show();
                deleteTask();
                Toast.makeText(getApplicationContext(),"Đã xoá task trống",Toast.LENGTH_SHORT).show();
            }
            else {
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    if (dueDate.getText().toString().length() > 0) {
                        if (!checkCompleted.isChecked()) {
                            if(task.getRepeat().equals("0000000")){
                                cancelScheduleAlarm(task.getIdTask());
                                Log.d("cancel repeat", " "+task.getIdTask() + task.getRepeat());
                            }
                            long wTime = Long.valueOf(dueDate.getText().toString()) - toMillis(hour, minutes, second);
                            if (wTime > 0) {
                                setReminder(wTime, task.getIdTask());
                                setRepeatAlarm(task.getRepeat(), wTime, task.getIdTask());
                                Log.d("task id", "update id " + id);
                            } else {
                                setReminder(86400000 + wTime, task.getIdTask());
                                setRepeatAlarm(task.getRepeat(),AlarmManager.INTERVAL_DAY*7 + wTime, task.getIdTask());
                            }
                        } else {
                            cancelReminder(task.getIdTask());
                            cancelScheduleAlarm(task.getIdTask());
                        }
                    } else {
                        cancelReminder(task.getIdTask());
                        cancelScheduleAlarm(task.getIdTask());
                    }
                }
            }
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //Xoá task
    private void deleteTask(){
        TextView textId = (TextView) findViewById(R.id.id_view);
        if(textId.getText().toString().length()>0){
            int id = Integer.valueOf(textId.getText().toString());
            Task task = toDoList.getTask(id);
            toDoList.deleteTask(task);
        }
//        Toast.makeText(getApplicationContext(),"Đã xoá",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void clearTime(){
        TextView dueDate = (TextView) findViewById(R.id.due_date);
        TextView showDate = (TextView) findViewById(R.id.showDate);
        dueDate.setText("");
        showDate.setText("");
        clearRepeatCheck();
        clrtime.setVisibility(View.GONE);
    }
    private void setReminder(long time,int id){
        Task taskget = toDoList.getTask(id);
        notificationReceiver = new Intent(todolist_item_Activity.this,
                reminderReceiver.class);
        notificationReceiver.putExtra("content alarm",taskget.getTaskDetails());
        Log.d("123",taskget.getTaskDetails());
        notificationReceiverPending = PendingIntent.getBroadcast(
                todolist_item_Activity.this, id, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + time,
                    notificationReceiverPending);
        }
        else {
            mAlarm.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + time,
                    notificationReceiverPending);
        }
//        mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, 60000, notificationReceiverPending);
        String timeHhMm = timeFormat(time);
        long second = Long.valueOf(60-Calendar.getInstance().get(Calendar.SECOND));
        String timeSs ="" + second;
        if(second == 60){
            timeHhMm = timeFormat(time - 60000);
            timeSs = ""+ (second - 1);
        }
        if(second < 10){
            timeSs = "0"+second;
        }
        Toast.makeText(getApplicationContext(), "Công việc này sẽ được nhắc nhở sau " + timeHhMm  + ":"+ timeSs,
                Toast.LENGTH_LONG).show();
    }
    public void scheduleAlarm(long time, int id, int alarmid){
        Task taskget = toDoList.getTask(id);
        notificationReceiver = new Intent(todolist_item_Activity.this,
                reminderReceiver.class);
        notificationReceiver.putExtra("content alarm",taskget.getTaskDetails());
        notificationReceiverPending = PendingIntent.getBroadcast(
                todolist_item_Activity.this, alarmid, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, AlarmManager.INTERVAL_DAY*7, notificationReceiverPending);
    }
    public void setRepeatAlarm(String str,long time, int id){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(str.substring(0, 1).equals("1") && (day - 2) != 0){
            scheduleAlarm(time+Math.abs(day-2)*86400000,id,-Integer.valueOf(id+"2"));
            Log.d("check setRepeatAlarm","Đã set repeat thứ 2 phát sau "+ Math.abs(day - 2));
        }
        if(str.substring(1, 2).equals("1") && (day - 3) != 0){
            scheduleAlarm(time+Math.abs(day-3)*86400000,id,-Integer.valueOf(id+"3"));
            Log.d("check setRepeatAlarm","Đã set repeat thứ 3 phát sau " + Math.abs(day - 3));
        }
        if(str.substring(2, 3).equals("1") && (day - 4) != 0){
            scheduleAlarm(time+Math.abs(day-4)*86400000,id,-Integer.valueOf(id+"4"));
            Log.d("check setRepeatAlarm","Đã set repeat thứ 4 phát sau " + Math.abs(day - 4));
        }
        if(str.substring(3, 4).equals("1") && (day - 5) != 0){
            scheduleAlarm(time+Math.abs(day-5)*86400000,id,-Integer.valueOf(id+"5"));
            Log.d("check setRepeatAlarm","Đã set repeat thứ 5 phát sau " + Math.abs(day - 5));
        }
        if(str.substring(4, 5).equals("1") && (day - 6) != 0){
            scheduleAlarm(time+Math.abs(day-6)*86400000,id,-Integer.valueOf(id+"6"));
            Log.d("check setRepeatAlarm","Đã set repeat thứ 6 phát sau " + Math.abs(day - 6));
        }
        if(str.substring(5, 6).equals("1") && (day - 7) != 0){
            scheduleAlarm(time+Math.abs(day-7)*86400000,id,-Integer.valueOf(id+"7"));
            Log.d("check setRepeatAlarm","Đã set repeat thứ 7 phát sau " + Math.abs(day - 7));
        }
        if(str.substring(6, 7).equals("1") && (day - 8) != 0){
            scheduleAlarm(time+Math.abs(day-1)*86400000,id,-Integer.valueOf(id+"8"));
            Log.d("check setRepeatAlarm","Đã set repeat CN phát sau " + Math.abs(day - 1));
        }
    }
    private void cancelReminder(int id){
        notificationReceiver = new Intent(todolist_item_Activity.this,
                reminderReceiver.class);
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            notificationReceiverPending = PendingIntent.getBroadcast(
                    todolist_item_Activity.this, id, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarm.cancel(notificationReceiverPending);
    }
    public void cancelScheduleAlarm(int id){
        int day = 2;
        notificationReceiver = new Intent(todolist_item_Activity.this,
                reminderReceiver.class);
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        while (day<=8) {
            notificationReceiverPending = PendingIntent.getBroadcast(
                    todolist_item_Activity.this, -Integer.valueOf(id + "" + day), notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("repeat cancle", " " + -Integer.valueOf(id + "" + day));
            mAlarm.cancel(notificationReceiverPending);
            day++;
        }
    }
    public long toMillis(int h, int m, int s){
        return Long.valueOf((h*3600 + m *60 + s)*1000);
    }
    public String timeFormat(long millis){
        String t = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        return t;
    }
    public void changeTheme(int loadThemeId){
        LinearLayout bgView = (LinearLayout)findViewById(R.id.layout_item);
        switch (loadThemeId){
            case 0:
                bgView.setBackgroundResource(R.drawable.wishlist_bg_image);
                break;
            case 1:
                bgView.setBackgroundResource(R.drawable.bg_todolist1);
                break;
            case 2:
                bgView.setBackgroundResource(R.drawable.bg_todolist2);
                break;
            case 3:
                bgView.setBackgroundResource(R.drawable.bg_todolist3);
                break;
        }
    }
    public void checkCondRepeat(){
        TextView showDate = (TextView) findViewById(R.id.showDate);
        CheckBox mon = (CheckBox)findViewById(R.id.mon);
        CheckBox tue = (CheckBox)findViewById(R.id.tue);
        CheckBox wed = (CheckBox)findViewById(R.id.wed);
        CheckBox thu = (CheckBox)findViewById(R.id.thu);
        CheckBox fri = (CheckBox)findViewById(R.id.fri);
        CheckBox sat = (CheckBox)findViewById(R.id.sat);
        CheckBox sun = (CheckBox)findViewById(R.id.sun);
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    mon.setChecked(false);
                }
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    tue.setChecked(false);
                }
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    wed.setChecked(false);
                }
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    thu.setChecked(false);
                }
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    fri.setChecked(false);
                }
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    sat.setChecked(false);
                }
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời điểm nhắc nhở", Toast.LENGTH_SHORT).show();
                    sun.setChecked(false);
                }
            }
        });
    }
    public void clearRepeatCheck(){
        TextView showDate = (TextView) findViewById(R.id.showDate);
        CheckBox mon = (CheckBox)findViewById(R.id.mon);
        CheckBox tue = (CheckBox)findViewById(R.id.tue);
        CheckBox wed = (CheckBox)findViewById(R.id.wed);
        CheckBox thu = (CheckBox)findViewById(R.id.thu);
        CheckBox fri = (CheckBox)findViewById(R.id.fri);
        CheckBox sat = (CheckBox)findViewById(R.id.sat);
        CheckBox sun = (CheckBox)findViewById(R.id.sun);
        if(showDate.getText().toString().length()<=0){
            mon.setChecked(false);
            tue.setChecked(false);
            wed.setChecked(false);
            thu.setChecked(false);
            fri.setChecked(false);
            sat.setChecked(false);
            sun.setChecked(false);
        }
    }
    public String getDateChecked(){
        String str = "";
        CheckBox mon = (CheckBox)findViewById(R.id.mon);
        CheckBox tue = (CheckBox)findViewById(R.id.tue);
        CheckBox wed = (CheckBox)findViewById(R.id.wed);
        CheckBox thu = (CheckBox)findViewById(R.id.thu);
        CheckBox fri = (CheckBox)findViewById(R.id.fri);
        CheckBox sat = (CheckBox)findViewById(R.id.sat);
        CheckBox sun = (CheckBox)findViewById(R.id.sun);
        if(mon.isChecked()){
            str = str + 1;
        } else str = str + 0;
        if(tue.isChecked()){
            str = str + 1;
        } else str = str + 0;
        if(wed.isChecked()){
            str = str + 1;
        } else str = str + 0;
        if(thu.isChecked()){
            str = str + 1;
        } else str = str + 0;
        if(fri.isChecked()){
            str = str + 1;
        } else str = str + 0;
        if(sat.isChecked()){
            str = str + 1;
        } else str = str + 0;
        if(sun.isChecked()){
            str = str + 1;
        } else str = str + 0;
        return str;
    }
    public void setDateChecked(String str){
        TextView showDate = (TextView) findViewById(R.id.showDate);
        CheckBox mon = (CheckBox)findViewById(R.id.mon);
        CheckBox tue = (CheckBox)findViewById(R.id.tue);
        CheckBox wed = (CheckBox)findViewById(R.id.wed);
        CheckBox thu = (CheckBox)findViewById(R.id.thu);
        CheckBox fri = (CheckBox)findViewById(R.id.fri);
        CheckBox sat = (CheckBox)findViewById(R.id.sat);
        CheckBox sun = (CheckBox)findViewById(R.id.sun);
        if(str.substring(0, 1).equals("1")){
            mon.setChecked(true);
        } else mon.setChecked(false);
        if(str.substring(1, 2).equals("1")){
            tue.setChecked(true);
        } else tue.setChecked(false);
        if(str.substring(2, 3).equals("1")){
            wed.setChecked(true);
        } else wed.setChecked(false);
        if(str.substring(3, 4).equals("1")){
            thu.setChecked(true);
        } else thu.setChecked(false);
        if(str.substring(4, 5).equals("1")){
            fri.setChecked(true);
        } else fri.setChecked(false);
        if(str.substring(5, 6).equals("1")){
            sat.setChecked(true);
        } else sat.setChecked(false);
        if(str.substring(6, 7).equals("1")){
            sun.setChecked(true);
        } else sun.setChecked(false);
    }
    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}