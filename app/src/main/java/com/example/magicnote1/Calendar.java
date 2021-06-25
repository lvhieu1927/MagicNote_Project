package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.magicnote1.dataconnect.MyDBHelperDiary;

import java.util.ArrayList;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class Calendar extends AppCompatActivity {

    private ArrayList<DateData> listDate;
    private MCalendarView calendarView;
    private ImageButton button1, button2, button3, button4, button5;
    private MyDBHelperDiary myDBHelperDiary = new MyDBHelperDiary(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        addControl();
        addEvent();
    }

    private void addEvent() {
        listDate = myDBHelperDiary.getListDate();

        for(int i=0;i<listDate.size();i++) {
            calendarView.markDate(listDate.get(i).getYear(),listDate.get(i).getMonth(),listDate.get(i).getDay());//mark multiple dates with this code.
            calendarView.setMarkedStyle(R.drawable.gradient);
        }

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("date",date.getDayString()+"/"+date.getMonthString()+"/"+date.getYear());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        setButtonMoodSearch();
    }

    private void setButtonMoodSearch()
    {
        button1 = findViewById(R.id.add_diary_button_1);
        button2 = findViewById(R.id.add_diary_button_2);
        button3 = findViewById(R.id.add_diary_button_3);
        button4 = findViewById(R.id.add_diary_button_4);
        button5 = findViewById(R.id.add_diary_button_5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("mood","happy");
                intent.putExtras(bundle);
                setResult(11,intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("mood","good");
                intent.putExtras(bundle);
                setResult(11,intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("mood","neutral");
                intent.putExtras(bundle);
                setResult(11,intent);
                finish();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("mood","awful");
                intent.putExtras(bundle);
                setResult(11,intent);
                finish();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("mood","bad");
                intent.putExtras(bundle);
                setResult(11,intent);
                finish();
            }
        });}

    private void addControl() {
        calendarView = findViewById(R.id.calendar);
    }
}