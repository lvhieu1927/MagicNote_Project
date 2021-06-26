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
                String lang,loadLang;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (checkedId){
                    case R.id.vi_radion:
                        loadLang = "vi";
                        lang = "Việt Nam";
                        Log.d("123", lang);
                        editor.putString("lang",loadLang);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), getString(R.string.ToastSetting1)+" " + lang + getString(R.string.ToastSetting2), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.en_radion:
                        loadLang = "en";
                        lang = "English";
                        editor.putString("lang",loadLang);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), getString(R.string.ToastSetting1)+" " + lang +getString(R.string.ToastSetting2), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}