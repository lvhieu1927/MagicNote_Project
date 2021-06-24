package com.example.magicnote1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    private ImageButton bt_AddDiary,bt_Home,bt_ChooseDate;
    CharSequence search="";
    static int flag = 0;
    private MyDBHelperDiary myDBHelperDiary = new MyDBHelperDiary(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_diary_main_menu);
        addControl();

        addEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String date = data.getStringExtra("date");
                ArrayList<DiaryNote> filteredList = new ArrayList<>();

                for (int i = 0; i < mListDiary.size(); i++) {
                    Log.d("Magic count size",mListDiary.size()+"");

                    String text = mListDiary.get(i).getDateText();
                    Log.d("Magic count size text",text);
                    Log.d("Magic count size date",date);
                    if (text.equals(date)) {

                        Log.d("Magic count",date+" +1+ "+text);
                        filteredList.add(mListDiary.get(i));
                    }
                }

                mRecyclerView.removeAllViews();;
                mDiaryNoteAdapter = new DiaryNoteAdapter(filteredList,MoodDiaryMainMenu.this);
                mRecyclerView.setAdapter(mDiaryNoteAdapter);
            }
        }
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

        addTextListener();
        bt_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MoodDiaryMainMenu.this,activity_home_screen.class);
                startActivity(intent1);
            }
        });

        bt_ChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag ==0) {
                    Intent intent2 = new Intent(MoodDiaryMainMenu.this, Calendar.class);
                    bt_ChooseDate.setImageResource(R.drawable.ic_clear);
                    flag=1;
                    startActivityForResult(intent2, 123);
                }
                else {
                    bt_ChooseDate.setImageResource(R.drawable.ic_date_range);
                    mRecyclerView.removeAllViews();;
                    mDiaryNoteAdapter = new DiaryNoteAdapter(mListDiary,MoodDiaryMainMenu.this);
                    mRecyclerView.setAdapter(mDiaryNoteAdapter);
                    flag=0;
                }

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
        bt_Home = findViewById(R.id.bt_home);
        bt_ChooseDate = findViewById(R.id.bt_DateChoose);
    }

    public void addTextListener(){

        searchInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();
                ArrayList<DiaryNote> filteredList = new ArrayList<>();

                for (int i = 0; i < mListDiary.size(); i++) {

                     String text = mListDiary.get(i).getNote().toLowerCase()+ mListDiary.get(i).getHeadline().toLowerCase();
                    if (text.contains(query)) {

                        Log.d("Magic count",query+" +1+ "+text);
                        filteredList.add(mListDiary.get(i));
                    }
                }

                mRecyclerView.removeAllViews();;
                mDiaryNoteAdapter = new DiaryNoteAdapter(filteredList,MoodDiaryMainMenu.this);
                mRecyclerView.setAdapter(mDiaryNoteAdapter);
            }
        });
    }
}