package com.example.magicnote1.model;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.magicnote1.R;

public class Buttonnew extends androidx.appcompat.widget.AppCompatButton {
    private int value;

    public Buttonnew(@NonNull Context context) {
        super(context);
        value = 0;
        setText("Button số ");
        setBackground(this.getResources().getDrawable(R.drawable.bt_custom1));
       setTextColor(Color.parseColor("#ffffff"));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;

        setLayoutParams(lp);
        setPadding(10,7,10,7);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getValue()==0)
                {
                    setValue(1,context);
                    setBackground(context.getDrawable(R.drawable.bt_custom1_choose));
                    setTextColor(Color.parseColor("#e67335"));
                }
                else
                {
                    setValue(0,context);
                    v.setBackground(context.getDrawable(R.drawable.bt_custom1));
                    setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
    }

    @Override
    public CharSequence getText() {
        return super.getText();
    }

    public void setValue(int value, Context context) {

        if (value==1)
        {
            this.value =1;
            setBackground(context.getDrawable(R.drawable.bt_custom1_choose));
            setTextColor(Color.parseColor("#e67335"));
        }
        else
        {
            this.value = 0;
            setBackground(context.getDrawable(R.drawable.bt_custom1));
            setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public int getValue() {
        return value;
    }
}
