package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.magicnote1.dataconnect.AssetDatabaseOpenHelper;
import com.example.magicnote1.dataconnect.MyDBHelperDiary;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.add_diary);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this,Add_Diary_1Activity.class);
                startActivity(i);
            }
        });
        AssetDatabaseOpenHelper assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(this);

        SQLiteDatabase sqLiteDatabase =  assetDatabaseOpenHelper.openDatabase();
        sqLiteDatabase.close();

        MyDBHelperDiary myDBHelperDiary = new MyDBHelperDiary(this,null,null,1);
        ArrayList<String> arrayList = myDBHelperDiary.getAllActivity();
        for (int i =0; i< arrayList.size(); i++)
        {
            Log.d("Magic!!!",arrayList.get(i));
        }

        Button btnToDoList =(Button)findViewById(R.id.todolist_button);
        btnToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, todolist_mainMenu.class);
                startActivity(intent);
            }
        });

    }
}