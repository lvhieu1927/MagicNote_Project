package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.magicnote1.adapter.DiaryNoteAdapter;
import com.example.magicnote1.dataconnect.MyDBHelperDiary;
import com.example.magicnote1.model.DiaryNote;

import java.util.ArrayList;

public class MoodDiaryMainMenu extends AppCompatActivity {

    private ArrayList<DiaryNote> mListDiary;
    private RecyclerView mRecyclerView;
    private DiaryNoteAdapter mDiaryNoteAdapter;
    private EditText searchInput;
    private ImageButton bt_AddDiary;
    CharSequence search="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_diary_main_menu);
        addControl();
        addEvent();
    }

    private void addEvent() {
        Intent  intent = new Intent(this,Add_Diary_1Activity.class);
        bt_AddDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                startActivity(intent);
            }
        });
        mDiaryNoteAdapter = new DiaryNoteAdapter(mListDiary, this);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                mDiaryNoteAdapter.getFilter().filter(s);
                search = s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addControl() {
        mRecyclerView = findViewById(R.id.rcv_main);
        MyDBHelperDiary helperDiary = new MyDBHelperDiary(this,null,null,1);
        mListDiary = helperDiary.getTop20DiaryNote();
        mDiaryNoteAdapter = new DiaryNoteAdapter(mListDiary,this);
        mRecyclerView.setAdapter(mDiaryNoteAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bt_AddDiary = findViewById(R.id.bt_AddDiary);
        searchInput = findViewById(R.id.search_input);
    }
}