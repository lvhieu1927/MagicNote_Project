package com.example.magicnote1;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magicnote1.adapter.WishListAdapter;
import com.example.magicnote1.dataconnect.WishListDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WishListMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton addButton;

    WishListDatabaseHelper myDB;
    ArrayList<String> itemId, itemName, itemPrice;
    WishListAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_main);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishListMainActivity.this, AddActivityWishList.class);
                startActivityForResult(intent,1);
            }
        });
        ActionBar actionBar = this.getActionBar();
        myDB = new WishListDatabaseHelper(WishListMainActivity.this);
        itemId = new ArrayList<>();
        itemName = new ArrayList<>();
        itemPrice = new ArrayList<>();

        displayInArray(); //goi ham hien thi cac item tren mang

        customAdapter = new WishListAdapter(this,this, itemId, itemName,itemPrice);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(WishListMainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate(); //khoi tao lai data
        }
    }

    //ham xu ly hien thi cac item tren mang
    void displayInArray(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){ //khong tim duoc data
            Toast.makeText(this,"No item.", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){ //tim thay data
                itemId.add(cursor.getString(0));
                itemName.add(cursor.getString(1));
                itemPrice.add(cursor.getString(2));
            }
        }
    }

    //ham xu ly xoa item tren menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(WishListMainActivity.this,"Search is expanded",Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(WishListMainActivity.this,"Search is collapsed",Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        menu.findItem(R.id.item_searchItem).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_searchItem).getActionView();
        searchView.setQueryHint("Search item here...");
        searchView.setBackground(this.getResources().getDrawable(R.drawable.bg_gradient));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_deleteAll){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Item?");
        builder.setMessage("Are you sure to delete all Item from WishList?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WishListDatabaseHelper myDB = new WishListDatabaseHelper(WishListMainActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(WishListMainActivity.this, WishListMainActivity.class);
                startActivity(intent);
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