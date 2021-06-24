package com.example.magicnote1.chart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.magicnote1.R;
import com.example.magicnote1.dataconnect.MyDBHelperDiary;
import com.example.magicnote1.model.DiaryNote;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;



public class PieChartActivity extends Activity {

    TextView textView1,textView2,textView3,textView4,textView5,tv_positive;
    MyDBHelperDiary dbHelperDiary;
    PieChart pieChart;
    LineChart lineChart;

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
        setPositive();
        setToLineChart();
    }

    private void addControl() {
        dbHelperDiary = new MyDBHelperDiary(this, null,null,1);
        pieChart = findViewById(R.id.piechart);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);
        textView5 = findViewById(R.id.textview5);
        tv_positive = findViewById(R.id.tv_positive);
        lineChart = findViewById(R.id.linechart);
    }

    private void  setToLineChart(){

        ArrayList<Integer> yAxisData = new ArrayList<>();
        ArrayList<DiaryNote> diaryNotes = dbHelperDiary.getTop20DiaryNote();
        for (int i=0; i <diaryNotes.size(); i++){
            yAxisData.add(diaryNotes.get(i).getMoodID());
        }
        ArrayList<Entry> arrayList = new ArrayList<>();

        for (int i=0; i < diaryNotes.size(); i++)
        {
            arrayList.add(new Entry(i,yAxisData.get(i)));
        }
        LineDataSet lineDataSet = new LineDataSet(arrayList,"country");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        LineData lineData;
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(30);
        lineChart.invalidate();
    }


    void setTextView()
    {
        textView1.setText(dbHelperDiary.countMood("happy")+"");
        textView2.setText(dbHelperDiary.countMood("good")+"");
        textView3.setText(dbHelperDiary.countMood("neutral")+"");
        textView4.setText(dbHelperDiary.countMood("awful")+"");
        textView5.setText(dbHelperDiary.countMood("bad")+"");
    }

    void setPositive()
    {
        tv_positive.setText(dbHelperDiary.getPositiveString());
    }


    private void setChart()
    {
        List<PieEntry> NoOfEmp = new ArrayList();
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("happy")*1f, 0));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("good")*1f, 1));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("neutral")*1f, 2));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("awful")*1f, 3));
        NoOfEmp.add(new PieEntry(dbHelperDiary.countMood("bad")*1f, 4));

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
        pieChart.setDrawSliceText(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setCenterText("Mood Diary");
        pieChart.setCenterTextColor(R.color.mauchu);

        pieChart.animateXY(5000, 5000);
    }
}
