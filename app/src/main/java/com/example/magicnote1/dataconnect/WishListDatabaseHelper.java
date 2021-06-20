package com.example.magicnote1.dataconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class WishListDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "WishList_DB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "wishList";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ITEM = "item_name";
    private static final String COLUMN_PRICE = "item_price";

    public WishListDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // tao bang dl
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM + " TEXT, " +
                COLUMN_PRICE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // cau hinh add function
    public void addItem(String name, String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ITEM, name);
        contentValues.put(COLUMN_PRICE, price);

        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to add to WishList!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added to WishList Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //tao con tro doc cac du lieu trong bang table
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //update tung dong du lieu
    public void updateData(String row_item_id, String item_name, String item_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM, item_name);
        cv.put(COLUMN_PRICE, item_price);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_item_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //thuc thi xoa mot dong du lieu
    public void deleteOneRow(String row_item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_item_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete from WishList!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Deleted from WishList Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //thuc thi xoa toan bo du lieu
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
