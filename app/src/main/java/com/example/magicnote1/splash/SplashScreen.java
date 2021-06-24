package com.example.magicnote1.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;
import com.example.magicnote1.activity_home_screen;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_DISPLAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // set splash screen toan man hinh

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,
                        activity_home_screen.class);
                //dung intent chuyen tu activity nay sang activity khac

                startActivity(i);
                //goi toi second activity

                finish();
                // ket thuc activity hien tai
            }
        }, SPLASH_SCREEN_DISPLAY);

    }
}