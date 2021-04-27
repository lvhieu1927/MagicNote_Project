package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class todolist_mainMenu extends AppCompatActivity {

    private ListView list;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_main_menu);
        Button btn_addItem = (Button) findViewById(R.id.add_item);
        btn_addItem.setOnClickListener((v) -> {
            Intent intent = new Intent(todolist_mainMenu.this,Add_itemActivity.class);
            startActivity(intent);
        });
        list = (ListView) findViewById(R.id.listView);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.test_list_item, items);
        list.setAdapter(adapter);
        items.add("item 1");
        items.add("item 2");
        if(items.isEmpty()){

        }

    }
}