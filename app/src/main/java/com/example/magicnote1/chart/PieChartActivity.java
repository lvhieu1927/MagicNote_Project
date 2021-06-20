package com.example.magicnote1.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magicnote1.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.chart_pie_layout,container,false);
        PieChart pieChart = view.findViewById(R.id.piechart);

        List<PieEntry> NoOfEmp = new ArrayList();

        NoOfEmp.add(new PieEntry(452, 0));
        NoOfEmp.add(new PieEntry(240, 1));
        NoOfEmp.add(new PieEntry(133, 2));
        NoOfEmp.add(new PieEntry(340, 3));
        NoOfEmp.add(new PieEntry(369, 4));

        NoOfEmp.get(0).setIcon(getResources().getDrawable(R.drawable.ic_happy2));
        NoOfEmp.get(1).setIcon(getResources().getDrawable(R.drawable.ic_good2));
        NoOfEmp.get(2).setIcon(getResources().getDrawable(R.drawable.ic_neutral2));
        NoOfEmp.get(3).setIcon(getResources().getDrawable(R.drawable.ic_sad2));
        NoOfEmp.get(4).setIcon(getResources().getDrawable(R.drawable.ic_bad2));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Mood");

        ArrayList year = new ArrayList();


        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
        pieChart.setCenterText("Mood Diary");
        pieChart.setCenterTextColor(R.color.mauchu);
        pieChart.setCenterTextSize(20);
        pieChart.animateXY(5000, 5000);
        return view;
    }
}
