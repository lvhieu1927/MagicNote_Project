package com.example.magicnote1.todolist;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.magicnote1.R;

import java.util.ArrayList;
import java.util.List;
//Activity quản lý task
public class todolist_MainMenu_Activity extends Activity {
    private ListView listViewTask;
    private List<Task> listTask;
    private ToDoList toDoList;
    private ImageView emptyView;
    private Handler handler;
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_layout, R.id.item_view, taskResult){
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
                        if(buttonView.isChecked()) {
                            listTask.get(position).setCompleted(true);
                            rowItem.setBackgroundResource(R.drawable.round_item_completed);

                        } else {
                            listTask.get(position).setCompleted(false);
                            rowItem.setBackgroundResource(R.drawable.round_item);
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
}