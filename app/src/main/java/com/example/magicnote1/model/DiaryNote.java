package com.example.magicnote1.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DiaryNote {

    //Class cấu hình để lưu trữ nhật ký thường ngày
    private String headline;
    private Integer diaryID;
    private Integer moodID;
    private String note;
    private byte[] Photo;
    private long date;

    private ArrayList<String> ActivityList;

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getHeadline() {
        return headline;
    }

    public long getDate() {
        return date;
    }


    public ArrayList<String> getActivityList() {
        return ActivityList;
    }

    public void setActivityList(ArrayList<String> activityList) {
        ActivityList = activityList;
    }


    public DiaryNote() {
        this.date = date;
        this.diaryID = 0;
        this.moodID = 0;
        this.note = "";
        this.Photo = (byte[])null;
    }

    public DiaryNote(Integer moodID, long date) {
        this.moodID = moodID;
        this.date = date;
        this.diaryID = 0;
        this.note = null;
        this.Photo = null;
    }

    public String getMoodName()
    {
        String moodName = null;
        int i = this.moodID;
        switch (i){
            case 1: return "HAPPY";
            case 2: return "GOOD";
            case 3: return "NEUTRAL";
            case 4: return "AWFUL";
        }
        return "BAD";
    }

    public Bitmap getBitmap()
    {
        if (this.getPhoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(this.getPhoto(), 0, this.getPhoto().length);
            return bitmap;
        }
        return null;
    }


    public void setDiaryID(Integer diaryID) {
        this.diaryID = diaryID;
    }

    public Integer getDiaryID() {
        return diaryID;
    }

    public void setMoodID(Integer moodID) {
        this.moodID = moodID;
    }

    public Integer getMoodID() {
        return moodID;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] photo) {
        Photo = photo;
    }
}
