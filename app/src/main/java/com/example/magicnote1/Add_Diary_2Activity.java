package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.magicnote1.model.Buttonnew;

import java.util.ArrayList;

public class Add_Diary_2Activity extends AppCompatActivity {

    private Buttonnew arrButton[];
    private HorizontalScrollView scr_Social, scr_Entertainment,scr_Sleep,scr_Food,scr_Calm,scr_Exercise,scr_Chores;
    LinearLayout layout_Social;
    int arrInt[] ;
    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__diary_2);
        addControl();
        addEvent();
    }

    private void addEvent() {
        createButtonOnSocial();
    }

    private void createButtonOnSocial() {
        ArrayList<Buttonnew> arrButton = new ArrayList<Buttonnew>();
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("Friends");
        arrString.add("Family");
        arrString.add("Date");
        arrString.add("Party");
        arrString.add("New");
        for (int i=0; i<5 ; i++)
        {
            Buttonnew buttonnew = new Buttonnew(this);
            buttonnew.setText(arrString.get(i));
            arrButton.add(buttonnew);

            layout_Social.addView(arrButton.get(i));
        }
    }

    private void addControl() {
        scr_Social = findViewById(R.id.scr_Social);
        scr_Entertainment = findViewById(R.id.scr_Entertainment);
        scr_Sleep = findViewById(R.id.scr_Sleep);
        scr_Calm = findViewById(R.id.scr_calm);
        scr_Food = findViewById(R.id.scr_Excercise);
        scr_Chores = findViewById(R.id.scr_Chores);
        scr_Exercise = findViewById(R.id.scr_Excercise);

        layout_Social = findViewById(R.id.layout_Social);
    }
}