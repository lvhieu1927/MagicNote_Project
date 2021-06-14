package com.example.magicnote1.dataconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magicnote1.R;
import com.example.magicnote1.model.DiaryNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiaryNoteAdapter extends RecyclerView.Adapter<DiaryNoteAdapter.ViewHolder>{

    private ArrayList<DiaryNote> mDiaryList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_Mood,tv_Activity,tv_Note,tv_Date;
        public ImageView img_Mood;
        public ImageButton img_Photo;
        private View itemview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemview = itemView;
            tv_Mood = (TextView) itemView.findViewById(R.id.tv_Mood);
            tv_Activity = (TextView) itemView.findViewById(R.id.tv_Activity);
            tv_Note = (TextView) itemView.findViewById(R.id.tv_Note);
            tv_Date = (TextView) itemView.findViewById(R.id.tv_date);
            img_Mood = (ImageView) itemView.findViewById(R.id.img_mood);
            img_Photo = itemView.findViewById(R.id.img_photo);
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
        holder.tv_Note.setText(diaryNote.getNote());
        holder.tv_Mood.setText(diaryNote.getMoodName());
        if (diaryNote.getActivityList().size() > 0) {
            String string = " "+diaryNote.getActivityList().get(0);
            for (int i = 1; i < diaryNote.getActivityList().size(); i++) {
                string += ", " + diaryNote.getActivityList().get(i);
            }
            holder.tv_Activity.setText(string);
        }

        holder.img_Photo.setImageBitmap(diaryNote.getBitmap());
        switch(diaryNote.getMoodID())
        {
            case 1: holder.img_Mood.setImageResource(R.drawable.ic_happy);
            holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.happy));
            break;
            case 2:holder.img_Mood.setImageResource(R.drawable.ic_good);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.good));
                break;
            case 3: holder.img_Mood.setImageResource(R.drawable.ic_neutral);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.neutral));
                break;
            case 4: holder.img_Mood.setImageResource(R.drawable.ic_awful);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.awful));
                break;
            case 5: holder.img_Mood.setImageResource(R.drawable.ic_bad);
                holder.tv_Mood.setTextColor(mContext.getResources().getColor(R.color.bad));
                break;
        }
        holder.tv_Date.setText(this.getDate(diaryNote.getDate(),"dd/MM/yyyy"));
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    public String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
