package com.example.magicnote1.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_DISPLAY = 2000;

    Animation topAnim, bottomAnim;
    TextView tv_title;
    ImageView intro_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen2);

        //Load animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.anim_top_down);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.anim_bottom_up);

        intro_imageView = (ImageView) findViewById(R.id.intro_imageView);
        tv_title = (TextView) findViewById(R.id.app_title);

        //cau hinh animation
        intro_imageView.setAnimation(topAnim);
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
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        }, SPLASH_SCREEN_DISPLAY);

    }

    public void fade(View view)
    {
        view.animate().alpha(0f).setDuration(1000);
    }
}