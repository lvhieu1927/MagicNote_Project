package com.example.magicnote1.dataconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Class này khởi tạo database
public class dbTask extends SQLiteOpenHelper {
    public static final String TABLE = "task";
    public static final String ID = "id";
    public static final String PRIORITY = "priority";
    public static final String DATE = "date";
    public static final String REPEAT = "repeat";
    public static final String TASK_DES = "tasks";
    public static final String COMPLETED = "completed";
    public static final String DB_NAME = "task.db";
    public static final int DB_VER = 1;
    private static final String DB_CREATE = "CREATE TABLE " + TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRIORITY + " INTEGER NOT NULL, "
            + DATE + " TEXT NOT NULL, "
            + REPEAT + " TEXT NOT NULL, "
            + TASK_DES + " TEXT NOT NULL, "
            + COMPLETED + " INTEGER NOT NULL );";
    public dbTask(Context context){
        super(context, DB_NAME, null, DB_VER);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
    }
    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL("drop table if exists " + TABLE);
        onCreate(db);
    }
}
