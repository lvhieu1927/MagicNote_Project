package com.example.magicnote1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.magicnote1.dataconnect.MyDBHelperDiary;
import com.example.magicnote1.model.DiaryNote;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Diary_3Activity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_CODE = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageButton bt_Photo,bt_Time;
    private Button bt_Date,bt_save;
    private Bitmap bitmap;
    private ImageView imageView,img_Mood;
    private TextInputEditText text_HeadLine;
    private EditText edt_Note;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;
    int selectedYear = 2000;
    int selectedMonth = 5;
    int selectedDayOfMonth = 10;
    private String str_Mood;
    private ArrayList<String> activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__diary_3);
        addControl();
        addEvent();
    }

    private void addControl() {
        bt_save = findViewById(R.id.bt_save);
        bt_Photo = findViewById(R.id.bt_Photo);
        bt_Date = findViewById(R.id.bt_date);
        bt_Time  = findViewById(R.id.bt_time);
        imageView = findViewById(R.id.img_photo);
        img_Mood = findViewById(R.id.img_mood);
        text_HeadLine = findViewById(R.id.text_headline);
        edt_Note = findViewById(R.id.edt_note);
    }



    private void addEvent() {
        setMood();
        setDate();
        Intent intent = new Intent(this,MoodDiaryMainMenu.class);

        bt_save.setOnClickListener(v -> {
            insertDataToDiary();
            insertDiary_Activity();

            startActivity(intent);
        });
        bt_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });
        bt_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectTime();
            }
        });
        bt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDatePickerButton();
            }
        });
    }

    private void setMood() {
        Intent intent = getIntent();
        String string = intent.getStringExtra("mood");
        str_Mood = string;
        bitmap = null;
        ArrayList<String> arrayList = intent.getStringArrayListExtra("activity");
        activity = arrayList;
        Log.d("magic!!!Mood",str_Mood);

        for (int i=0; i< activity.size(); i++)
        {
            Log.d("magic!!!Activity",activity.get(i));
        }
        
        switch(string){
            case "happy":
                img_Mood.setImageResource(R.drawable.ic_happy_white);
                break;
            case "good":
                img_Mood.setImageResource(R.drawable.ic_good_white);
                break;
            case "neutral":
                img_Mood.setImageResource(R.drawable.ic_neutral_white);
                break;
            case "awful":
                img_Mood.setImageResource(R.drawable.ic_awful_white);
                break;
            case "bad":
                img_Mood.setImageResource(R.drawable.ic_bad_white);
                break;
        }

    }
    
    private void setDate()
    {
        final Calendar c = Calendar.getInstance();
        this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
        this.lastSelectedMinute = c.get(Calendar.MINUTE);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        selectedYear = Integer.parseInt(currentDate.substring(6,10));
        selectedMonth = Integer.parseInt(currentDate.substring(3,5));
        selectedDayOfMonth = Integer.parseInt(currentDate.substring(0,2));
        Log.d("magic!!!",currentDate.substring(0,2)+" "+currentDate.substring(3,5)+" "+currentDate.substring(6,10));
        bt_Date.setText(currentDate);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){// e.g. "back" pressed"
            Uri contentURI = Uri.parse(data.getDataString());
            ContentResolver cr = getContentResolver();
            InputStream in = null;
            try {
                in = cr.openInputStream(contentURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=16;
            bitmap = BitmapFactory.decodeStream(in,null,options);
            imageView.setImageBitmap(bitmap);
        }
    }


    public void insertDataToDiary()
    {
        MyDBHelperDiary helperDiary = new MyDBHelperDiary(this,null,null,1);
        DiaryNote diaryNote = new DiaryNote();
        diaryNote.setMoodID(helperDiary.getMoodIdByName(str_Mood));
        diaryNote.setDate(getTime());
        diaryNote.setHeadline(text_HeadLine.getText().toString());
        diaryNote.setNote(edt_Note.getText().toString());
        diaryNote.setPhoto(getBitmapAsByteArray(bitmap));
        helperDiary.insertDiaryNote(diaryNote);
    }

    public void insertDiary_Activity()
    {
        MyDBHelperDiary helperDiary = new MyDBHelperDiary(this,null,null,1);
        int diary_id = helperDiary.getLastDiaryNoteID();
        for (int i = 0; i < activity.size(); i++)
        {
            int activity_id = helperDiary.getActivityIdByActivityName(activity.get(i));
            helperDiary.insertDiary_Activity(diary_id, activity_id);
        }
    }


    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if(grantResults.length >= 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //enable to open camera
                }
                else{
                    // denided permission
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void buttonSelectTime() {
            // Get Current Time


        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("magic!!!", hourOfDay + ":" + minute);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };
        TimePickerDialog timePickerDialog = null;
        timePickerDialog = new TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                timeSetListener, lastSelectedHour, lastSelectedMinute, true);
        timePickerDialog.show();
    }

    private void onDatePickerButton()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                bt_Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                selectedDayOfMonth = dayOfMonth;
                selectedMonth = monthOfYear+1;
                selectedYear = year;
            }
        };

// Create DatePickerDialog (Spinner Mode):
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

// Show
        datePickerDialog.show();
    }

    private long getTime()
    {
        if (lastSelectedMinute == -1){
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }
        String date ="1-1-2000 0:0:0";
        date = selectedDayOfMonth + "-" + selectedMonth + "-" + selectedYear + " " + lastSelectedHour + ":" + lastSelectedMinute +":00";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date datex = null;
        try {
            datex = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMilliseconds = datex.getTime();
        return timeInMilliseconds;
    }
}
