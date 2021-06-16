package com.example.magicnote1.model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.magicnote1.Add_Diary_2Activity;
import com.example.magicnote1.R;

public class DialogChooseMood extends Dialog  {
    public Context context;
    private ImageButton button1, button2, button3, button4, button5;
    private DialogChooseMood.MoodListener listener;

    public interface MoodListener {
        public void moodChosen(String mood);
    }

    public DialogChooseMood(Context context, DialogChooseMood.MoodListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_mood);

        button1 = findViewById(R.id.add_diary_button_1);
        button2 = findViewById(R.id.add_diary_button_2);
        button3 = findViewById(R.id.add_diary_button_3);
        button4 = findViewById(R.id.add_diary_button_4);
        button5 = findViewById(R.id.add_diary_button_5);

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
    }
    //nút bấm chuyển tới Add_Diary_2Activity kèm theo mood
    public void onClickButton(String buttonString) {
        this.dismiss(); // Close Dialog

        if(this.listener!= null)  {
            this.listener.moodChosen(buttonString);
        }
    }
}
