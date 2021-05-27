package com.example.magicnote1.dataconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.magicnote1.model.DiaryNote;

import java.util.ArrayList;

public class MyDBHelperDiary extends SQLiteOpenHelper {

    //class dùng để liên lạc với các dữ liệu của Diary Emotion
    private SQLiteDatabase db;
    Context context;
    private AssetDatabaseOpenHelper helper;



    public MyDBHelperDiary(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        helper = new AssetDatabaseOpenHelper(this.context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    //hàm lấy tất cả activity
    public ArrayList<String> getAllActivity()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        db = helper.openDatabase();
        String sql = "select * from activity";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext())
        {
            arrayList.add(cursor.getString(2));
        }
        db.close();
        return arrayList;
    }

    //hàm trả về tên activity theo định danh id
    public String getActivityNameById(int activity_id)
    {
        db = helper.openDatabase();
        String activity_name;
        Cursor cursor = db.rawQuery("select activity_name from activity where activity_id =?",new String[]{activity_id+""});
        cursor.moveToNext();
        activity_name = cursor.getString(0);
        db.close();
        return activity_name;
    }

    //hàm trả về mã định danh theo tên hoạt động
    public int getActivityIdByActivityName(String name)
    {
        db = helper.openDatabase();
        int activity_id;
        Cursor cursor = db.rawQuery("select activity_id from activity where activity_name =?",new String[]{name+""});
        cursor.moveToNext();
        activity_id = cursor.getInt(0);
        db.close();
        return activity_id;
    }

    public int getMoodIdByName(String name)
    {
        db = helper.openDatabase();
        int mood_Id;
        Cursor cursor = db.rawQuery("select mood_id from mood where mood_name =?",new String[]{name});
        cursor.moveToNext();
        mood_Id = cursor.getInt(0);
        db.close();
        return mood_Id;
    }

    //hàm đưa dữ liệu vào database bảng diary
    public void insertDiaryNote(DiaryNote diaryNote)
    {
        db = helper.openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mood_id",diaryNote.getMoodID());
        contentValues.put("headline",diaryNote.getHeadline());
        contentValues.put("date",diaryNote.getDate());
        contentValues.put("photo",diaryNote.getPhoto());
        contentValues.put("note",diaryNote.getNote());
        contentValues.put("voice", (byte[]) null);
        db.insert("diary",null,contentValues);
        db.close();
    }

    public int getLastDiaryNoteID()
    {
        db = helper.openDatabase();
        String sql = "select diary_id from diary order by diary_id desc limit 1 ";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext())
        {
            return cursor.getInt(0);
        }
        return 0;
    }

    //hàm lấy 20 dòng dữ liệu gần nhất của nhật ký
    public ArrayList<DiaryNote> getTop20DiaryNote()
    {
        ArrayList<DiaryNote> arrayList = new ArrayList<DiaryNote>();
        db = helper.openDatabase();
        String sql = "select * from diary LIMIT 20";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext())
        {
            DiaryNote diaryNote = new DiaryNote();
            diaryNote.setDiaryID(cursor.getInt(0));
            diaryNote.setMoodID(cursor.getInt(1));
            diaryNote.setHeadline(cursor.getString(2));
            diaryNote.setDate(cursor.getLong(3));
            diaryNote.setNote(cursor.getString(4));
            diaryNote.setPhoto(cursor.getBlob(5));
            arrayList.add(diaryNote);
        }
        return arrayList;
    }
}
