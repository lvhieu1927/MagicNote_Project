package com.example.magicnote1.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;

import java.util.ArrayList;

public class Add_itemActivity extends Activity {
    private dbTask dbTask;
    private ListView listViewTask;
    private ArrayAdapter<String> adapter;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        dbTask = new dbTask(this);
        Button btn_back = (Button) findViewById(R.id.backToDoList);
        Button addDone = (Button)findViewById(R.id.done);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void handleClickButton(View v){
        switch (v.getId()) {
            case R.id.done:
                final EditText titleText = (EditText) findViewById(R.id.title_text);
                final EditText desText = (EditText) findViewById(R.id.desc_text);
                SQLiteDatabase db = dbTask.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskWork.taskForm.TASK_TITLE, titleText.getText().toString());
//                values.put(TaskWork.taskForm.TASK_DESC, desText.getText().toString());
                db.insertWithOnConflict(TaskWork.taskForm.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                titleText.setText("");
        }
    }
}