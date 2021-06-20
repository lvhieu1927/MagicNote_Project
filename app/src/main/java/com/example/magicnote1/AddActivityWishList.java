package com.example.magicnote1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.dataconnect.WishListDatabaseHelper;

public class AddActivityWishList extends AppCompatActivity {
    EditText name, price;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}