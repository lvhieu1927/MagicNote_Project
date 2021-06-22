package com.example.magicnote1.dataconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.example.magicnote1.model.Task;

import java.util.ArrayList;
import java.util.List;

//Class này giao tiếp với database và xử lý dữ liệu
public class TaskWorkDBA {
    private SQLiteDatabase database;
    private dbTask dbHelper;
    private String[] column = { dbTask.ID, dbTask.PRIORITY, dbTask.DATE, dbTask.REPEAT, dbTask.TASK_DES, dbTask.COMPLETED};
    public TaskWorkDBA(Context context){
        dbHelper = new dbTask(context);
    }
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
    public Task createTask(Task task){
        ContentValues values = new ContentValues();
        int priority = 0;
        if(task.getPriority()){
            priority = 1;
        }
        int done = 0;
        if(task.getCompleted()){
            done = 1;
        }
        values.put(dbTask.PRIORITY, priority);
        values.put(dbTask.DATE, task.getDate());
        values.put(dbTask.REPEAT, task.getDate());
        values.put(dbTask.TASK_DES,task.getTaskDetails());
        values.put(dbTask.COMPLETED, done);
        long insertId = database.insert(dbTask.TABLE, null, values);
        Cursor cursor = database.query(dbTask.TABLE, column, dbTask.ID + " = " + insertId, null,null,null,null);
        cursor.moveToLast();
        Task newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
    }
    public void deleteTask(Task task){
        int id = task.getIdTask();
        database.delete(dbTask.TABLE, dbTask.ID + " = " + id,null);
    }
    public void updateTask(Task task){
        ContentValues values = new ContentValues();
        int id = task.getIdTask();
        int priority = 0;
        if(task.getPriority()){
            priority = 1;
        }
        int done = 0;
        if(task.getCompleted()){
            done = 1;
        }
        values.put(dbTask.PRIORITY,priority);
        values.put(dbTask.COMPLETED,done);
        values.put(dbTask.TASK_DES,task.getTaskDetails());
        values.put(dbTask.DATE,task.getDate());
        values.put(dbTask.REPEAT,task.getDate());
        database.update(dbTask.TABLE, values, dbTask.ID + " = " + id,null);
    }
    public List<Task> getAllTask(){
        List <Task> taskList = new ArrayList<Task>(0);
        Cursor cursor = database.query(dbTask.TABLE,column, null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Task task = cursorToTask(cursor);
            taskList.add((task));
            cursor.moveToNext();
        }
        return taskList;
    }
    public Task cursorToTask(Cursor cursor){
        int id = cursor.getInt(0);
        int priority = cursor.getInt(1);
        String date = cursor.getString(2);
        String repeat = cursor.getString(3);
        String taskString = cursor.getString(4);
        int complete = cursor.getInt(5);
        Task task = new Task();
        task.setIdTask(id);
        task.setPriority(priority == 1);
        task.setDate(date);
        task.setRepeat(repeat);
        task.setTaskDetails(taskString);
        task.setCompleted(complete == 1);
        return task;
    }
    public Task getTaskbyId(int id){
        Cursor cursor = database.query(dbTask.TABLE, column, dbTask.ID + " = " + id, null,null,null,null);
        return (cursor.moveToFirst() ? cursorToTask(cursor) : null);
    }
}
