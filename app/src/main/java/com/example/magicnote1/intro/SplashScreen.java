package com.example.magicnote1.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_DISPLAY = 4000;

    Animation topAnim, bottomAnim;
    TextView tv_title;
    GifImageView gif_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen2);

        //Load animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.anim_top_down);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.anim_bottom_up);

        gif_imageView = (GifImageView) findViewById(R.id.app_gif);
        tv_title = (TextView) findViewById(R.id.app_title);

        //cau hinh animation
        gif_imageView.setAnimation(topAnim);
        tv_title.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,
                        IntroductionActivity.class);
                //dung intent chuyen tu activity nay sang activity khac

                startActivity(i);
                //goi toi second activity

                finish();
                // ket thuc activity hien tai
            }
        }, SPLASH_SCREEN_DISPLAY);

    }
}