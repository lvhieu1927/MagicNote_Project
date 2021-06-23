package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.magicnote1.dataconnect.MyDBHelperDiary;

import java.util.ArrayList;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class Calendar extends AppCompatActivity {

    private ArrayList<DateData> listDate;
    private MCalendarView calendarView;
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
                bundle.putString("date",date.getDay()+"/"+date.getMonthString()+"/"+date.getYear());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void addControl() {
        calendarView = findViewById(R.id.calendar);
    }
}