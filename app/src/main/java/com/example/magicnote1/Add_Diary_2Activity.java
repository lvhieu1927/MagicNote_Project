package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.magicnote1.model.Buttonnew;

import java.util.ArrayList;

public class Add_Diary_2Activity extends AppCompatActivity {

    private String str_Mood;
    private ImageView img_Mood;
    private LinearLayout ml_Social,ml_Entertainment,ml_Sleep,ml_Food,ml_Calm,ml_Excercise,ml_Chores;
    private ArrayList<Buttonnew> arrButton;
    private Button bt_Save, bt_Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__diary_2);
        addControl();
        addEvent();
    }

    private void addControl() {
        ml_Social = findViewById(R.id.layout_Social);
        ml_Entertainment = findViewById(R.id.layout_Entertainment);
        ml_Sleep = findViewById(R.id.layout_Sleep);
        ml_Food = findViewById(R.id.layout_Food);
        ml_Calm = findViewById(R.id.layout_Calm);
        ml_Excercise = findViewById(R.id.layout_Excercise);
        ml_Chores = findViewById(R.id.layout_Chores);
        img_Mood = findViewById(R.id.img_mood);
        bt_Save = findViewById(R.id.bt_save);
        bt_Next = findViewById(R.id.bt_next);
        arrButton = new ArrayList<Buttonnew>();
    }

    private void addEvent() {
        setMood();
        createButtonOnSocial();
        createButtonOnEntertainment();
        createButtonOnSleep();
        createButtonOnFood();
        createButtonOnCalm();
        createButtonOnExercise();
        createButtonOnChores();
        bt_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextData();
            }
        });
    }

    private void nextData()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < arrButton.size(); i++)
        {
            if (arrButton.get(i).getValue() == 1)
            {
                arrayList.add(arrButton.get(i).getText().toString());
            }
        }
        Intent  intent = new Intent(this,Add_Diary_3Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mood",str_Mood);
        bundle.putStringArrayList("activity",arrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //khởi tạo biểu cảm ở đầu layout theo intent được truyền
    private void setMood() {
        Intent intent = getIntent();
        String string = intent.getStringExtra("mood");
        str_Mood = string;
        switch(string){
            case "happy":
                img_Mood.setImageResource(R.drawable.ic_happy_white);
                break;
            case "good":
                img_Mood.setImageResource(R.drawable.ic_good_white);
                break;
            case "neutral":
                img_Mood.setImageResource(R.drawable.ic_neutral_white);
                break;
            case "awful":
                img_Mood.setImageResource(R.drawable.ic_awful_white);
                break;
            case "bad":
                img_Mood.setImageResource(R.drawable.ic_bad_white);
                break;
        }

    }

    //tạo mảng các thành viên cho mục Social
    private void createButtonOnSocial() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("friends");
        arrString.add("family");
        arrString.add("date");
        arrString.add("party");
        arrString.add("new friend");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Social.addView(buttonnew);
        }
    }
    //tạo mảng các thành viên cho mục Entertainment
    private void createButtonOnEntertainment() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("movies");
        arrString.add("tv show");
        arrString.add("gaming");
        arrString.add("reading");
        arrString.add("board");
        arrString.add("shopping");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Entertainment.addView(buttonnew);
        }
    }
    //tạo mảng các thành viên cho mục Sleep
    private void createButtonOnSleep() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("well sleep");
        arrString.add("average sleep");
        arrString.add("bad sleep");
        arrString.add("nightmare");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Sleep.addView(buttonnew);
        }
    }
    //tạo mảng các thành viên cho mục Food
    private void createButtonOnFood() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("sugar");
        arrString.add("fast food");
        arrString.add("delivery food");
        arrString.add("home cooking");
        arrString.add("dining");
        arrString.add("water");
        arrString.add("healthy");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Food.addView(buttonnew);
        }
    }
    //tạo mảng các thành viên cho mục Calm
    private void createButtonOnCalm() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("meditation");
        arrString.add("relax");
        arrString.add("bath");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Calm.addView(buttonnew);
        }
    }
    //tạo mảng các thành viên cho mục Exercise
    private void createButtonOnExercise() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("gym");
        arrString.add("home exercise");
        arrString.add("running");
        arrString.add("walking");
        arrString.add("cycling");
        arrString.add("weights");
        arrString.add("swimming");
        arrString.add("yoga");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Excercise.addView(buttonnew);
        }
    }

    //tạo mảng các thành viên cho mục Chores
    private void createButtonOnChores() {
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("cleaning");
        arrString.add("gardening");
        arrString.add("laundry");
        arrString.add("dishes");
        arrString.add("cooking");
        arrString.add("home");
        arrString.add("groceries");
        for (int i=0; i<arrString.size() ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);
            ml_Chores.addView(buttonnew);
        }
    }
}