package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.magicnote1.model.Buttonnew;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Add_Diary_1Activity extends AppCompatActivity {

    TextView hello_text;
    Button button1, button2, button3, button4, button5;
    private TextClock textClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__diary_1);
        addControl();
        addEvents();
    }

    // Hàm để khai báo các thành phần được sử dụng trong activity
    public void addControl() {
        hello_text = (TextView) findViewById(R.id.hello_text);
        button1 = findViewById(R.id.add_diary_button_1);
        button2 = findViewById(R.id.add_diary_button_2);
        button3 = findViewById(R.id.add_diary_button_3);
        button4 = findViewById(R.id.add_diary_button_4);
        button5 = findViewById(R.id.add_diary_button_5);
        this.textClock = (TextClock) this.findViewById(R.id.myTextClock);
    }

    // Hàm để thêm các event cho activity
    public void addEvents() {
        //set textClock định dạng
        this.textClock.setFormat24Hour(null);
        //
    }

    //nút bấm chuyển tới Add_Diary_2Activity kèm theo mood
    public void onClick(View view) {
        Button button = (Button)view;
        String buttonString = button.getText().toString();
        Intent intent = new Intent(this,Add_Diary_2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mood",buttonString);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    // Hàm để thay đổi hình theme tự động theo thời gian
    private void changeTheme()
    {
        int i = getTimeFromAndroid();
        hello_text.setText(set_hello_text(i)+"\nLet's start with your mood");
        if (i == 1 ) hello_text.setBackgroundResource(R.drawable.add_diary_1_activity_1_morning);
        if (i == 2 ) hello_text.setBackgroundResource(R.drawable.add_diary_1_activity_2_afternoon);
        if (i == 3 ) hello_text.setBackgroundResource(R.drawable.add_diary_1_activity_3_evening);
        if (i == 4 ) hello_text.setBackgroundResource(R.drawable.add_diary_1_activity_4_night);
    }

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
    public String set_hello_text(int i)
    {
        if (i == 1 )
            return "Good morning!";
        if (i == 2)
            return  "Good afternoon!";
        if (i == 3)
            return  "Good evening!";
        return "Good Night!";
    }

}