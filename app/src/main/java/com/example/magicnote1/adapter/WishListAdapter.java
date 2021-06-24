package com.example.magicnote1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magicnote1.R;
import com.example.magicnote1.UpdateActivityWishList;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList item_id, item_name, item_price;
    private int count =1;
    //khai bao contractor voi cac doi so truyen vao
    public WishListAdapter(Activity activity, Context context, ArrayList id, ArrayList name, ArrayList price){
        this.activity = activity;
        this.context = context;
        this.item_id = id;
        this.item_name = name;
        this.item_price = price;
    }


    @NonNull
    @Override
    public WishListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_item_row_wishlist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.MyViewHolder holder, int position) {
        holder.item_id.setText((position+1)+"");
        holder.item_name.setText(String.valueOf(item_name.get(position)));
        holder.item_price.setText(String.valueOf(item_price.get(position)));

        // thuc thi ham khi nhan chon mot phan tu recyclerview
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivityWishList.class);
                intent.putExtra("id", String.valueOf(item_id.get(position)));
                intent.putExtra("name", String.valueOf(item_name.get(position)));
                intent.putExtra("price", String.valueOf(item_price.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_id.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_id, item_name, item_price;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_id = itemView.findViewById(R.id.item_id);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            //tao animation tren recyclerview
            

        }
    }
}
