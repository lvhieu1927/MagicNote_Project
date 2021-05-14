package com.example.magicnote1.todolist;

import android.content.Context;

import com.example.magicnote1.todolist.Task;
import com.example.magicnote1.todolist.db.TaskWorkDBA;

import java.util.ArrayList;
import java.util.List;
//Class này dựa trên class TaskWorkDBA để tạo các function
public class ToDoList {
    private List<Task> toDoList;
    private TaskWorkDBA taskDBA;

    public ToDoList(Context context) {
        toDoList = new ArrayList<Task>(0);
        taskDBA = new TaskWorkDBA(context);
        taskDBA.open();
    }
    public Task getTask(int id){
        return taskDBA.getTaskbyId(id);
    }
    public Task createTask(Task task){
        return taskDBA.createTask(task);
    }
    public void updateTask(Task task){
        taskDBA.updateTask(task);
    }
    public void deleteTask(Task task){
        taskDBA.deleteTask(task);
    }
    public List<Task> getAllTask(){
        toDoList = taskDBA.getAllTask();
        return toDoList;
    }
}
