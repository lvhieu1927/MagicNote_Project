package com.example.magicnote1.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.magicnote1.Add_Diary_3Activity;
import com.example.magicnote1.R;
import com.example.magicnote1.model.DiaryNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiaryNoteAdapter extends RecyclerView.Adapter<DiaryNoteAdapter.ViewHolder> implements Filterable {

    private ArrayList<DiaryNote> mDiaryList;
    private ArrayList<DiaryNote> mData;
    private Context mContext;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView tv_Mood, tv_Activity, tv_Note, tv_Date;
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
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    public DiaryNoteAdapter(ArrayList<DiaryNote> _DiaryList, Context _Context) {
        mDiaryList = _DiaryList;
        mContext = _Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View diaryView = layoutInflater.inflate(R.layout.acctivity_add_diary_recycle_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(diaryView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryNote diaryNote = mDiaryList.get(position);
        holder.img_Mood.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        holder.tv_Mood.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        holder.tv_Note.setText(diaryNote.getNote());
        holder.tv_Mood.setText(diaryNote.getMoodName());
        if (diaryNote.getActivityList().size() > 0) {
            String string = "[" + diaryNote.getActivityList().get(0);
            for (int i = 1; i < diaryNote.getActivityList().size(); i++) {
                string += "], [" + diaryNote.getActivityList().get(i);
            }
            string += "]";
            holder.tv_Activity.setText(string);
        }

        holder.img_Photo.setImageBitmap(diaryNote.getBitmap());
        switch (diaryNote.getMoodID()) {
            case 1:
                holder.img_Mood.setImageResource(R.drawable.ic_happy2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.happy));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 2:
                holder.img_Mood.setImageResource(R.drawable.ic_good2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.good));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 3:
                holder.img_Mood.setImageResource(R.drawable.ic_neutral2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.neutral));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 4:
                holder.img_Mood.setImageResource(R.drawable.ic_sad2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.awful));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
            case 5:
                holder.img_Mood.setImageResource(R.drawable.ic_bad2);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.bad));
                holder.tv_Activity.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                holder.tv_Header.setTextColor(mContext.getResources().getColor(R.color.mauchu));
                break;
        }
        holder.tv_Header.setText("Headline: " + diaryNote.getHeadline());
        holder.tv_Date.setText(this.getDate(diaryNote.getDate(), "dd/MM/yyyy"));

        //set button cho gọi tới màn hình sửa
        Intent intent = new Intent(mContext, Add_Diary_3Activity.class);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 1);
                    bundle.putInt("diary_id", mDiaryList.get(position).getDiaryID());
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

    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDiaryList = mData;

                } else {
                    ArrayList<DiaryNote> lstFiltered = new ArrayList<>();
                    for (DiaryNote row : mData) {

                        if (row.getHeadline().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }

                    }

                    mDiaryList = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = mDiaryList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                mDiaryList = (ArrayList<DiaryNote>) results.values;
                notifyDataSetChanged();

            }
        };

    }
}
