package com.example.magicnote1.todolist;
//Class định nghĩa Task
public class Task {
    private int idTask;
    private boolean priority;
    private String date;
    private String task;
    private boolean isCompleted;

    public Task(){
    }
    //Set
    public void setIdTask(int id) {
        this.idTask = id;
    }

    public void setPriority(boolean p) {
        this.priority = p;
    }

    public void setDate(String d) {
        this.date = d;
    }

    public void setTaskDetails(String t) {
        this.task = t;
    }

    public void setCompleted(boolean c) {
        isCompleted = c;
    }

    //Get
    public int getIdTask() {
        return idTask;
    }

    public boolean getPriority() {
        return priority;
    }

    public String getDate() {
        return date;
    }

    public String getTaskDetails() {
        return task;
    }

    public boolean getCompleted() {
        return isCompleted;
    }
    public String result(){
        String result = "";
        if(priority){
            result += "Important: ";
        }
        result += task + " - time: " + date;
        return result;
    }
}
