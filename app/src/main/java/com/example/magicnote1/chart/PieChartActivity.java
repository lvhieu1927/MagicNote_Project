package com.example.magicnote1.chart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.magicnote1.R;
import com.example.magicnote1.dataconnect.MyDBHelperDiary;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends Activity {

    TextView textView1,textView2,textView3,textView4,textView5;
    MyDBHelperDiary dbHelperDiary;
    PieChart pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_pie_layout);
        addControl();
        addEvent();
    }

    private void addEvent() {
        setChart();
        setTextView();
    }

    private void addControl() {
        dbHelperDiary = new MyDBHelperDiary(this, null,null,1);
        pieChart = findViewById(R.id.piechart);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);
        textView5 = findViewById(R.id.textview5);
    }

    void setTextView()
    {
        textView1.setText(dbHelperDiary.countMood("happy")+"");
        textView2.setText(dbHelperDiary.countMood("good")+"");
        textView3.setText(dbHelperDiary.countMood("neutral")+"");
        textView4.setText(dbHelperDiary.countMood("awful")+"");
        textView5.setText(dbHelperDiary.countMood("bad")+"");
    }

    void setChart()
    {
        List<PieEntry> NoOfEmp = new ArrayList();
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("happy")*1f, 0));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("good")*1f, 1));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("neutral")*1f, 2));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("awful")*1f, 3));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("bad")*1f, 4));

        Log.d("Magic count",dbHelperDiary.countMood("happy")+"");
        Log.d("Magic count",dbHelperDiary.countMood("good")+"");
        Log.d("Magic count",dbHelperDiary.countMood("neutral")+"");
        Log.d("Magic count",dbHelperDiary.countMood("awful")+"");
        Log.d("Magic count",dbHelperDiary.countMood("bad")+"");

        NoOfEmp.get(0).setLabel("HAPPY");
        NoOfEmp.get(1).setLabel("GOOD");
        NoOfEmp.get(2).setLabel("NEUTRAL");
        NoOfEmp.get(3).setLabel("AWFUL");
        NoOfEmp.get(4).setLabel("BAD");


        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
        PieData data = new PieData(dataSet);
        dataSet.setColors(getResources().getIntArray(R.array.manymood));

        pieChart.setData(data);
        pieChart.setCenterTextSize(15);
        pieChart.setEntryLabelTextSize(13);
        pieChart.setCenterTextSizePixels(13);
        pieChart.setScrollBarSize(20);

        pieChart.setCenterText("Mood Diary");
        pieChart.setCenterTextColor(R.color.mauchu);

        pieChart.animateXY(4000, 4000);
    }
}
