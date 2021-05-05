package com.example.magicnote1.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;

public class Add_itemActivity extends AppCompatActivity {
    EditText inputText;
    Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Button btn_back = (Button) findViewById(R.id.backToDoList);
        Button addDone = (Button)findViewById(R.id.done);

        addDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_itemActivity.this, todolist_mainMenu.class);
                intent.putExtra("1","4");
                startActivityForResult(intent,12);
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}