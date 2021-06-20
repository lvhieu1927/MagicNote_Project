package com.example.magicnote1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.concurrent.TimeUnit;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_main_menu);
        loadingLogo();
        listTask = new ArrayList<Task>(0);
        toDoList = new ToDoList(this);
        listViewTask = (ListView)findViewById(R.id.lisk_view_task);
        updateListViewTask();
        listViewTask.setOnItemClickListener(listViewListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateListViewTask();
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
                Switch switchDone = (Switch) rowItem.findViewById(R.id.switch_done);
                TextView itemView = (TextView)rowItem.findViewById(R.id.item_view);
                itemView.setText(listTask.get(position).result());
                if(listTask.get(position).getCompleted())
                {
                    switchDone.setChecked(true);
                    rowItem.setBackgroundResource(R.drawable.round_item_completed);
                }
                switchDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Calendar calendar = Calendar.getInstance();
                        notificationReceiver = new Intent(todolist_MainMenu_Activity.this,
                                reminderReceiver.class);
                        notificationReceiverPending = PendingIntent.getBroadcast(
                                todolist_MainMenu_Activity.this, 0, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                        if(buttonView.isChecked()) {
                            listTask.get(position).setCompleted(true);
                            rowItem.setBackgroundResource(R.drawable.round_item_completed);
                            //mAlarm.cancel(notificationReceiverPending);
                        } else {
                            listTask.get(position).setCompleted(false);
                            rowItem.setBackgroundResource(R.drawable.round_item);
//                            long wTime = Long.valueOf(listTask.get(position).getDate()) - toMillis(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//                            if (wTime > 0) {
//                                setReminder(wTime);
//                            } else setReminder(86460000 + wTime);
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
        ImageView logo =(ImageView)findViewById(R.id.logo);
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
        },700);
    }
    private void setReminder(long time){
        mAlarm.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + time,
                notificationReceiverPending);
        Toast.makeText(getApplicationContext(), "Công việc này sẽ được nhắc nhở sau " + timeFormat(time)+":" + Long.valueOf(60- Calendar.getInstance().get(Calendar.SECOND)),
                Toast.LENGTH_LONG).show();
    }
    public long toMillis(int h, int m, int s){
        return Long.valueOf((h*3600 + m *60 + s)*1000);
    }
    public String timeFormat(long millis){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }
}