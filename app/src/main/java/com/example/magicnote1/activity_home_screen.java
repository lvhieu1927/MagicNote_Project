package com.example.magicnote1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.magicnote1.chart.PieChartActivity;
import com.example.magicnote1.splash.SplashScreenWishlist;

public class activity_home_screen extends AppCompatActivity {

    private CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        addControl();
        addEvent();
    }

    private void addEvent() {
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this,todolist_MainMenu_Activity.class);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this,MoodDiaryMainMenu.class);
                startActivityForResult(intent,2);
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this,todolist_MainMenu_Activity.class);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this, SplashScreenWishlist.class);
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_screen.this, PieChartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        cardView1 = findViewById(R.id.card1);
        cardView2 = findViewById(R.id.card2);
        cardView3 = findViewById(R.id.card3);
        cardView4 = findViewById(R.id.card4);
        cardView5 = findViewById(R.id.card5);
        cardView6 = findViewById(R.id.card6);
    }



}