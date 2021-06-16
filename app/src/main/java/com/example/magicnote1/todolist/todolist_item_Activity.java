package com.example.magicnote1.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magicnote1.R;

import java.util.ArrayList;

//Activity thao tác với task
public class todolist_item_Activity extends Activity {
    private ToDoList toDoList;
    private AlarmManager mAlarm;
    private Intent notificationReceiver,mLoggerReceiverIntent;
    private PendingIntent notificationReceiverPending,mLoggerReceiverPendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_item);
        toDoList = new ToDoList(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        ImageButton buttonAdd = (ImageButton)findViewById(R.id.button_add_task);
        ImageButton buttonEdit = (ImageButton)findViewById(R.id.button_update_task);
        ImageButton buttonDelete = (ImageButton)findViewById(R.id.button_delete_task);
        if(id != 0){
            buttonAdd.setVisibility(View.GONE);
            loadTaskData(id);
        }
        else{
            buttonAdd.setVisibility(View.VISIBLE);
            buttonEdit.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }
        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationReceiver = new Intent(todolist_item_Activity.this,
                reminderReceiver.class);
        notificationReceiverPending = PendingIntent.getBroadcast(
                todolist_item_Activity.this, 0, notificationReceiver, 0);

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
                deleteTask();
                break;
            case R.id.button_back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.button_speak:
                EditText des = (EditText)findViewById(R.id.task_description);
                Intent intentStt = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentStt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
                try {
                    startActivityForResult(intentStt,1);
                    des.setText("");
                }
                catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Oops",Toast.LENGTH_SHORT).show();
                }
        }
    }
    //Load data của task
    private void loadTaskData(int id){
        TextView textId = (TextView)findViewById(R.id.id_view);
        CheckBox checkPriority = (CheckBox)findViewById(R.id.CBpriority);
        EditText dueDate = (EditText) findViewById(R.id.due_date);
        EditText description = (EditText)findViewById(R.id.task_description);
        CheckBox checkCompleted = (CheckBox)findViewById(R.id.CBcompleted);
        Task task = toDoList.getTask(id);
        textId.setText(String.valueOf(task.getIdTask()));
        checkPriority.setChecked(task.getPriority());
        dueDate.setText(task.getDate());
        description.setText(task.getTaskDetails());
        checkCompleted.setChecked(task.getCompleted());
    }
    //Thêm task
    private void addTask(){
        TextView textId = (TextView)findViewById(R.id.id_view);
        CheckBox checkPriority = (CheckBox)findViewById(R.id.CBpriority);
        EditText dueDate = (EditText) findViewById(R.id.due_date);
        EditText description = (EditText)findViewById(R.id.task_description);
        CheckBox checkCompleted = (CheckBox)findViewById(R.id.CBcompleted);
        Task task = new Task();
        task.setPriority(checkPriority.isChecked());
        task.setDate(dueDate.getText().toString());
        task.setTaskDetails(description.getText().toString());
        task.setCompleted(checkCompleted.isChecked());
        Task taskNew = toDoList.createTask(task);
        textId.setText(String.valueOf(taskNew.getIdTask()));
        //Kiểm tra important
        if (checkPriority.isChecked()) {
            setReminder(Integer.valueOf(dueDate.getText().toString()));
        }

        //Kiểm tra có nội dung hay không, nếu không có thì xoá
        if(task.getTaskDetails().length() == 0){
            Toast.makeText(getApplicationContext(),"Bạn cần nhập vào nội dung để có thể thêm",Toast.LENGTH_SHORT).show();
            deleteTask();
            Toast.makeText(getApplicationContext(),"Đã xoá task trống",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //Cập nhật task
    private void updateTask(){
        TextView textId = (TextView)findViewById(R.id.id_view);
        CheckBox checkPriority = (CheckBox)findViewById(R.id.CBpriority);
        EditText dueDate = (EditText) findViewById(R.id.due_date);
        EditText description = (EditText)findViewById(R.id.task_description);
        CheckBox checkCompleted = (CheckBox)findViewById(R.id.CBcompleted);
        if(textId.getText().toString().length()>0){
            int id = Integer.valueOf(textId.getText().toString());
            Task task = toDoList.getTask(id);
            task.setPriority(checkPriority.isChecked());
            task.setDate(dueDate.getText().toString());
            task.setTaskDetails(description.getText().toString());
            task.setCompleted(checkCompleted.isChecked());
            if(dueDate.getText().toString().length()<=0) {
                if(checkPriority.isChecked()) {
                    dueDate.setError("Nếu quan trong, không được để trống ô này");
                    return;
                }
            } else setReminder(Integer.valueOf(dueDate.getText().toString()));
            toDoList.updateTask(task);
            textId.setText(String.valueOf(task.getIdTask()));

            //Kiểm tra có nội dung hay không, nếu không có thì xoá
            if(task.getTaskDetails().length() == 0){
                Toast.makeText(getApplicationContext(),"Bạn cần có nội dung task",Toast.LENGTH_SHORT).show();
                deleteTask();
                Toast.makeText(getApplicationContext(),"Đã xoá task trống",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
    //Xoá task
    private void deleteTask(){
        TextView textId = (TextView) findViewById(R.id.id_view);
        if(textId.getText().toString().length()>0){
            int id = Integer.valueOf(textId.getText().toString());
            Task task = toDoList.getTask(id);
            toDoList.deleteTask(task);
        }
        Toast.makeText(getApplicationContext(),"Đã xoá",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void setReminder(int time){
        mAlarm.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + time,
                notificationReceiverPending);
        Toast.makeText(getApplicationContext(), "Single Alarm Set",
                Toast.LENGTH_LONG).show();
    }
}