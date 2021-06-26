package com.example.magicnote1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class setting_Activity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Button bt_Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
        setContentView(R.layout.activity_setting);
        settingLangguage();
        bt_Save = (Button)findViewById(R.id.button_update_setting);
        bt_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.d("123",lang);
            }
        });
    }
    public void settingLangguage(){
        RadioGroup langGroup = (RadioGroup)findViewById(R.id.lang_radion);
        langGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String language,loadLang;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (checkedId){
                    case R.id.vi_radion:
                        loadLang = "vi";
                        language = getString(R.string.vietnam);
                        Log.d("123", language);
                        editor.putString("lang",loadLang);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), getString(R.string.ToastSetting1)+" " + language + getString(R.string.ToastSetting2), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.en_radion:
                        loadLang = "en";
                        language = getString(R.string.english);
                        editor.putString("lang",loadLang);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), getString(R.string.ToastSetting1)+" " + language +getString(R.string.ToastSetting2), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
    public void settingTodolist(){
        String todolistOpt1,todolistOpt2;
        CheckBox opt1 = (CheckBox)findViewById(R.id.todolist_opt1);
        CheckBox op2 = (CheckBox)findViewById(R.id.todolist_opt2);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        opt1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                    editor.putBoolean("todolistOpt1",true);
                    editor.apply();
                }
                else {
                    editor.putBoolean("todolistOpt1",false);
                    editor.apply();
                }
            }
        });
        op2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                }
                else {

                }
            }
        });
    }
    public void settingEmotionDiary(){

    }
    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}