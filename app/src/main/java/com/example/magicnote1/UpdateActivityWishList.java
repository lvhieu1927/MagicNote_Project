package com.example.magicnote1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.dataconnect.WishListDatabaseHelper;

public class UpdateActivityWishList extends AppCompatActivity {
    EditText itemName_input, itemPrice_input;
    Button updateButton, deleteButton;
    String id, name, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_wishlist);

        //dinh danh cac id tuong ung
        itemName_input = findViewById(R.id.itemName_input2);
        itemPrice_input = findViewById(R.id.itemPrice_input2);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        //goi ham cau hinh intent data
        getAndSetIntentData();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name); //hien thi ten cac item tren actionBar
        }

        //bat su kien khi click chon button update
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //And only then we call this
                WishListDatabaseHelper myDB = new WishListDatabaseHelper(UpdateActivityWishList.this);
                name = itemName_input.getText().toString().trim();
                price = itemPrice_input.getText().toString().trim();
                myDB.updateData(id, name, price);
                finish();
            }
        });

        //bat su kien khi click chon button delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog(); //ham xac nhan xoa item
            }
        });

    }

    //ham lay va cau hinh intent data
    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("price")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            price = getIntent().getStringExtra("price");

            //cau hinh intent data
            itemName_input.setText(name);
            itemPrice_input.setText(price);
            Log.d("stev", name+" "+price);
        }else{
            Toast.makeText(this, "No item.", Toast.LENGTH_SHORT).show();
        }
    }

    //ham xac nhan xoa item
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure to delete " + name + " from WishList?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WishListDatabaseHelper myDB = new WishListDatabaseHelper(UpdateActivityWishList.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}