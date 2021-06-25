package com.example.magicnote1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.dataconnect.WishListDatabaseHelper;

import java.util.Locale;

public class AddActivityWishList extends AppCompatActivity {
    EditText name, price;
    Button addButton;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
        setContentView(R.layout.activity_addwishlist);

        //dinh danh cac id tuong ung
        name = findViewById(R.id.itemName_input);
        price = findViewById(R.id.itemPrice_input);
        addButton = findViewById(R.id.add_button);

        //bat su kien khi nhan vao button them
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishListDatabaseHelper myDB = new WishListDatabaseHelper(AddActivityWishList.this);
                myDB.addItem(name.getText().toString().trim(),
                        price.getText().toString().trim());
                finish();
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