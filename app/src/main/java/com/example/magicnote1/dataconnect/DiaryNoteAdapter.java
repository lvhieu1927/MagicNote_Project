package com.example.magicnote1.dataconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.magicnote1.Add_Diary_3Activity;
import com.example.magicnote1.R;
import com.example.magicnote1.model.DiaryNote;

import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiaryNoteAdapter extends RecyclerView.Adapter<DiaryNoteAdapter.ViewHolder> {

    private ArrayList<DiaryNote> mDiaryList;
    private Context mContext;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView tv_Mood,tv_Activity,tv_Note,tv_Date;
        public ImageView img_Mood;
        public ImageView img_Photo;
        public RelativeLayout container;
        private View itemview;
        private ItemClickListener itemClickListener;
        public TextView tv_Header;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemview = itemView;
            tv_Mood = (TextView) itemView.findViewById(R.id.tv_Mood);
            tv_Activity = (TextView) itemView.findViewById(R.id.tv_Activity);
            tv_Note = (TextView) itemView.findViewById(R.id.tv_Note);
            tv_Date = (TextView) itemView.findViewById(R.id.tv_date);
            img_Mood = (ImageView) itemView.findViewById(R.id.img_mood);
            img_Photo = itemView.findViewById(R.id.img_photo);
            container = itemView.findViewById(R.id.container);
            tv_Header = itemView.findViewById(R.id.tv_Header);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
    }

    public DiaryNoteAdapter(ArrayList<DiaryNote> _DiaryList, Context _Context)
    {
        mDiaryList = _DiaryList;
        mContext = _Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View diaryView = layoutInflater.inflate(R.layout.acctivity_add_diary_recycle_view_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(diaryView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryNote diaryNote = mDiaryList.get(position);
        holder.img_Mood.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        holder.tv_Mood.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        holder.tv_Note.setText(diaryNote.getNote());
        holder.tv_Mood.setText(diaryNote.getMoodName());
        if (diaryNote.getActivityList().size() > 0) {
            String string = "["+diaryNote.getActivityList().get(0);
            for (int i = 1; i < diaryNote.getActivityList().size(); i++) {
                string += "], [" + diaryNote.getActivityList().get(i);
            }
            string+="]";
            holder.tv_Activity.setText(string);
        }

        holder.img_Photo.setImageBitmap(diaryNote.getBitmap());
        switch(diaryNote.getMoodID())
        {
            case 1: holder.img_Mood.setImageResource(R.drawable.ic_happy2);
            holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.happy));
            holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
            holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
            break;
            case 2:holder.img_Mood.setImageResource(R.drawable.ic_good2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.good));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 3: holder.img_Mood.setImageResource(R.drawable.ic_neutral2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.neutral));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 4: holder.img_Mood.setImageResource(R.drawable.ic_sad2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.awful));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 5: holder.img_Mood.setImageResource(R.drawable.ic_bad2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.bad));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
        }
        holder.tv_Header.setText("Headline: "+diaryNote.getHeadline());
        holder.tv_Date.setText(diaryNote.getDateText());

        //set button cho gọi tới màn hình sửa
        Intent intent = new Intent(mContext, Add_Diary_3Activity.class);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){}
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag",1);
                    bundle.putInt("diary_id",mDiaryList.get(position).getDiaryID());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }


}
