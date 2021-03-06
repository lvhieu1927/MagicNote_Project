package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class Add_Diary_1Activity extends AppCompatActivity {

    TextView hello_text,textView2;
    ImageButton button1, button2, button3, button4, button5,bt_Exit;
    private TextClock textClock;
    private String language;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
        language = lang;
        setContentView(R.layout.activity_add__diary_1);
        addControl();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
    }

    // Hàm để khai báo các thành phần được sử dụng trong activity
    public void addControl() {
        hello_text = (TextView) findViewById(R.id.hello_text);
        textView2 = findViewById(R.id.text2);
        button1 = findViewById(R.id.add_diary_button_1);
        button2 = findViewById(R.id.add_diary_button_2);
        button3 = findViewById(R.id.add_diary_button_3);
        button4 = findViewById(R.id.add_diary_button_4);
        button5 = findViewById(R.id.add_diary_button_5);
        bt_Exit = findViewById(R.id.bt_Exit);
        this.textClock = (TextClock) this.findViewById(R.id.myTextClock);
    }

    // Hàm để thêm các event cho activity
    public void addEvents() {
        //set textClock định dạng
        this.textClock.setFormat24Hour(null);
        //
        if (language.equals("en"))
        {
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/font_1_dancingscript_regular.otf");
            hello_text.setTypeface(face);
            textView2.setTypeface(face);
        }
        else
        {
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/SVN-Bellico.otf");
            hello_text.setTypeface(face);
            textView2.setTypeface(face);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton("happy");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton("good");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton("neutral");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton("awful");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton("bad");
            }
        });
        Intent intent = new Intent(this,MoodDiaryMainMenu.class);
        bt_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    //nút bấm chuyển tới Add_Diary_2Activity kèm theo mood
    public void onClickButton(String buttonString) {
        Intent intent = new Intent(this,Add_Diary_2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mood",buttonString);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    // Hàm để thay đổi hình theme tự động theo thời gian

    //Hàm lấy thời gian thực của máy
    private int getTimeFromAndroid() {
        Calendar rightNow = Calendar.getInstance();
        int hours = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        System.out.println("harry porter"+hours);
        if(hours>=1 && hours<=12){
            return 1;
        }else if(hours>=12 && hours<=16){
            return 2;
        }else if(hours>=16 && hours<=21){
            return 3;
        }
        return 4;
    }

    //Hàm lấy nội dung câu chào theo thời gian buổi
    public void set_hello_text(int i)
    {
        if (i == 1 )
            hello_text.setText(R.string.goodmorning + R.string.AddDiary1_Hello1);
        if (i == 2)
            hello_text.setText(R.string.goodmorning + R.string.AddDiary1_Hello1);
        if (i == 3)
            hello_text.setText(R.string.goodmorning + R.string.AddDiary1_Hello1);
        if (i == 4)
            hello_text.setText(R.string.goodmorning + R.string.AddDiary1_Hello1);
    }
    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (local.equals("vi"))
        {

        }
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}