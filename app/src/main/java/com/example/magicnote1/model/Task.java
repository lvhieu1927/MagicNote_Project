package com.example.magicnote1.model;

import java.util.concurrent.TimeUnit;

//Class định nghĩa Task
public class Task {
    private int idTask;
    private boolean priority;
    private String date;
    private String repeat;
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

    public void setRepeat(String r) {
        this.repeat = r;
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

    public String getRepeat() {
        return repeat;
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
        if(date.length() > 0) {
            result += task + " at: " + timeFormat(Long.valueOf(date));
        } else result += task;
        return result;
    }
    public String timeFormat(long millis){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }
}
