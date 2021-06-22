package com.example.magicnote1.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.magicnote1.R;
import com.example.magicnote1.activity_home_screen;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroductionActivity extends AppCompatActivity {

    private ViewPager viewScreenPager;
    ViewPageAdapter viewPagerAdapter ;
    TabLayout tabIndicator;
    Button btn_next;
    int position = 0 ;
    Button btn_getStarted;
    Animation btn_animation ;
    Button textView_skip;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tao full sreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ktra activity khoi chayj truoc do hay chua
        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), activity_home_screen.class );
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_introduction);

        // ini views
        btn_next = findViewById(R.id.buttonNext);
        btn_getStarted = findViewById(R.id.btn_Start);
        tabIndicator = findViewById(R.id.indicator_Tab);
        btn_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_anim);
        textView_skip = findViewById(R.id.tv_skip);

        // truyen data tuong ung
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Diary Mood","Ghi lại cảm xúc người dùng hằng ngày",R.drawable.img_diarymode));
        mList.add(new ScreenItem("To Do List","Liệt kê các công việc dự định hoàn thành",R.drawable.img_todolist));
        mList.add(new ScreenItem("WishList"," Danh sách sản phẩm yêu thích của người dùng",R.drawable.img_wishlist));

        // tao viewpager
        viewScreenPager =findViewById(R.id.screen_viewpager);
        viewPagerAdapter = new ViewPageAdapter(this,mList);
        viewScreenPager.setAdapter(viewPagerAdapter);

        // khoi tao tablayout bang viewpager

        tabIndicator.setupWithViewPager(viewScreenPager);

        // bat su kien click btn next

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewScreenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    viewScreenPager.setCurrentItem(position);
                }

                if (position == mList.size()-1) { // man hinh cuoi
                    // hien thi get started button va an cac thanh phan khac
                    DisplayLastScreen();

                }

            }
        });

        // tablayout add change listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {
                    DisplayLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // bat su kien click started button
        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mo activity home screen
                Intent mainActivity = new Intent(getApplicationContext(),activity_home_screen.class);
                startActivity(mainActivity);

                //lưu giá trị vào bộ nhớ để lần sau khi người dùng chạy ứng dụng
                savePrefsData();
                finish();
            }
        });

        // bat su kien click vao skip button
        textView_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewScreenPager.setCurrentItem(mList.size());
            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    // luu data truoc
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }

    // hien thi get started button va an cac thanh phan khac
    private void DisplayLastScreen() {
        btn_next.setVisibility(View.INVISIBLE);
        btn_getStarted.setVisibility(View.VISIBLE);
        textView_skip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btn_getStarted.setAnimation(btn_animation);
    }
}