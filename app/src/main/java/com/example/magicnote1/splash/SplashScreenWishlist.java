package com.example.magicnote1.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;
import com.example.magicnote1.WishListMainActivity;

public class SplashScreenWishlist extends AppCompatActivity {
    private static int SPLASH_SCREEN_SHOWING = 2000; //thoi  gian chay splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_wishlist);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenWishlist.this,
                        WishListMainActivity.class);
                //dung intent chuyen tu activity nay sang activity khac

                startActivity(i);
                //goi toi second activity

                finish();
                // ket thuc activity hien tai
            }
        }, SPLASH_SCREEN_SHOWING);
    }
}