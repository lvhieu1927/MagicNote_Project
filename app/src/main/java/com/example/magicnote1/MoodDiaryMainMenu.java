package com.example.magicnote1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.magicnote1.adapter.DiaryNoteAdapter;
import com.example.magicnote1.dataconnect.MyDBHelperDiary;
import com.example.magicnote1.model.DiaryNote;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MoodDiaryMainMenu extends AppCompatActivity {

    private ArrayList<DiaryNote> mListDiary;
    private RecyclerView mRecyclerView;
    private DiaryNoteAdapter mDiaryNoteAdapter;
    private EditText searchInput;
    private ImageButton bt_AddDiary,bt_Home,bt_ChooseDate, bt_ChangeTheme;
    private ImageView emptyView;
    private SharedPreferences sharedPreferences;
    CharSequence search="";
    static int flag = 0;
    private MyDBHelperDiary myDBHelperDiary = new MyDBHelperDiary(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
        setContentView(R.layout.activity_mood_diary_main_menu);
        addControl();
        addEvent();
        checkEmpty();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
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

                    String text = mListDiary.get(i).getDateText();
                    if (text.equals(date)) {

                        filteredList.add(mListDiary.get(i));
                    }
                }

                mRecyclerView.removeAllViews();;
                mDiaryNoteAdapter = new DiaryNoteAdapter(filteredList,MoodDiaryMainMenu.this);
                mRecyclerView.setAdapter(mDiaryNoteAdapter);
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                String date = data.getStringExtra("mood");
                ArrayList<DiaryNote> filteredList = new ArrayList<>();

                for (int i = 0; i < mListDiary.size(); i++) {

                    String text = mListDiary.get(i).getMoodName();
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
                Intent intent12 = new Intent(MoodDiaryMainMenu.this,activity_home_screen.class);
                intent12.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent12);
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
        bt_ChangeTheme = (ImageButton) findViewById(R.id.change_theme_button) ;
        bt_ChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int loadThemeId = sharedPreferences.getInt("themeIdDiary",0);
                Log.d("id at click"," "+loadThemeId);
                loadThemeId++;
                if(loadThemeId>3){
                    loadThemeId = 0;
                }
                changeTheme(loadThemeId);
                Log.d("id after click"," "+loadThemeId);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("themeIdDiary",loadThemeId);
                editor.apply();
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
    public void changeTheme(int loadThemeId){
        LinearLayout bgView = (LinearLayout)findViewById(R.id.linearAll);
        switch (loadThemeId){

            case 0:
                bgView.setBackgroundResource(R.drawable.wishlist_bg_image);
                break;
            case 1:
                bgView.setBackgroundResource(R.drawable.bg_todolist1);
                break;
            case 2:
                bgView.setBackgroundResource(R.drawable.bg_todolist2);
                break;
            case 3:
                bgView.setBackgroundResource(R.drawable.bg_todolist3);
                break;
        }
    }

    public void checkEmpty(){
        emptyView = (ImageView) findViewById(R.id.empty_view);
        if(mDiaryNoteAdapter.getItemCount()>0){
            emptyView.setVisibility(View.GONE);
        }
        else emptyView.setVisibility(View.VISIBLE);
    }
    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}