package com.example.magicnote1.model;

public class DiaryNote {

    //Class cấu hình để lưu trữ nhật ký thường ngày
    private String headline;
    private Integer diaryID;
    private Integer moodID;
    private String note;
    private byte[] Photo;
    private long date;

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getHeadline() {
        return headline;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
