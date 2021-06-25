package com.example.magicnote1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.magicnote1.dataconnect.MyDBHelperDiary;
import com.example.magicnote1.model.Buttonnew;
import com.example.magicnote1.model.DialogChooseMood;
import com.example.magicnote1.model.DiaryNote;
import com.example.magicnote1.model.ToDoList;
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
    private static final int REQUEST_ACTIVITY = 121;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageButton bt_Photo;
    private ImageButton bt_Time,bt_clearPhoto,bt_Exit;
    private Button bt_Change_Activity;
    private LinearLayout layout_Activity;
    private Button bt_Date,bt_save,bt_changeMood;
    private Bitmap bitmap = null;
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
    int diary_ID_Receiver = 0;
    private ToDoList toDoList;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__diary_3);
        addControl();
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES_NAME", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","en");
        setAppLang(lang);
        addEvent();

    }

    //hàm khai báo các định danh cho biến
    private void addControl() {
        bt_save = findViewById(R.id.bt_save);
        bt_Photo = findViewById(R.id.bt_Photo);
        bt_Date = findViewById(R.id.bt_date);
        bt_Time  = findViewById(R.id.bt_time);
        imageView = findViewById(R.id.img_photo);
        img_Mood = findViewById(R.id.img_mood);
        text_HeadLine = findViewById(R.id.text_headline);
        edt_Note = findViewById(R.id.edt_note);
        bt_changeMood = findViewById(R.id.bt_ChangeMood);
        bt_Change_Activity = (Button)findViewById(R.id.bt_Change_Activity);
        bt_clearPhoto = findViewById(R.id.bt_clearPhoto);
        layout_Activity = findViewById(R.id.layout_activity);
        bt_Exit = findViewById(R.id.bt_Exit);

    }


    //hàm dùng bắt sự kiện
    private void addEvent() {

        switch (check())
        {
            case 0:
                setMood();
                setDate();
                scrollActivitySet();
                setButtonExit();
                break;
            case 1:
                setUpUpdate(diary_ID_Receiver);
                break;
            case 2:
                setTodayNote();
                break;
        }

        Intent intent = new Intent(this,MoodDiaryMainMenu.class);

        bt_save.setOnClickListener(v -> {
            insertOrUpdateDataToDiary();
            insertDiary_Activity();
            if (check() == 2) {
                setResult(Activity.RESULT_OK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
            else {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
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
        bt_clearPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                bitmap = null;
            }
        });

        bt_changeMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangeMood();
            }
        });

        bt_Change_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangeActivity();
            }
        });
    }

    //lấy dữ liệu ngày hôm nay của todolist
    private void setTodayNote() {
        bt_Exit.setImageResource(R.drawable.ic_clear);
        img_Mood.setImageResource(R.drawable.ic_neutral_white);
        str_Mood  = "neutral";
        setDate();
        text_HeadLine.setText("TodoList of day");
        Intent intent = getIntent();
        String todayNote = intent.getStringExtra("goToDiary");
        edt_Note.setText(todayNote);
        bt_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTodo = new Intent();
                setResult(Activity.RESULT_CANCELED,intentTodo);
                intentTodo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });
    }

    //cài đặt ban đầu cho màn hình update
    private void setUpUpdate(int diary_id)
    {
        bt_Exit.setImageResource(R.drawable.ic_baseline_delete_forever_24);
        MyDBHelperDiary dbHelperDiary = new MyDBHelperDiary(this,null,null,1);
        DiaryNote diaryNote = dbHelperDiary.getDiaryNoteFromID(diary_id);
        str_Mood = diaryNote.getMoodName();
        switch(str_Mood){
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
        text_HeadLine.setText(diaryNote.getHeadline());
        activity  = diaryNote.getActivityList();
        edt_Note.setText(diaryNote.getNote());
        imageView.setImageBitmap(diaryNote.getBitmap());
        scrollActivitySet();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(diaryNote.getDate());
        String dateString = formatter.format(calendar.getTime());
        Context context = this;
        selectedYear = Integer.parseInt(dateString.substring(6,10));
        selectedMonth = Integer.parseInt(dateString.substring(3,5));
        selectedDayOfMonth = Integer.parseInt(dateString.substring(0,2));
        lastSelectedHour= Integer.parseInt(dateString.substring(11,13));
        lastSelectedMinute = Integer.parseInt(dateString.substring(14,16));
        Log.d("magic!!!","time:" +lastSelectedHour+":"+lastSelectedMinute );
        bt_Date.setText(selectedDayOfMonth+"/"+selectedMonth+"/" + selectedYear);
        bt_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gọi phương thức xóa note diary tại đây\
                dialog_Yes_No(context);
            }
        });
    }

    //nút thoát khỏi tiến trình hiện tại
    private void setButtonExit()
    {
        bt_Exit.setImageResource(R.drawable.ic_clear2);
        Intent intent = new Intent(this,MoodDiaryMainMenu.class);
        bt_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    //nút gọi đén add2 cho phép chọn lại activity
    private void buttonChangeActivity()
    {
        Intent intent = new Intent(this, Add_Diary_2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("flag",1);
        bundle.putStringArrayList("activity",activity);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_ACTIVITY);
    }

    //nút cho phép chọn lại mood
    private void buttonChangeMood()
    {
        DialogChooseMood.MoodListener listener = new DialogChooseMood.MoodListener() {
            @Override
            public void moodChosen(String mood) {
                str_Mood = mood;
                switch(mood){
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
        };
        final DialogChooseMood dialogChooseMood = new DialogChooseMood(this,listener);
        dialogChooseMood.show();
    }

    //thêm các activity bằng nút vào scrollbar activity
    private void scrollActivitySet()
    {
        if (activity != null)
            for (int i=0; i<activity.size(); i++)
            {
                Buttonnew buttonnew = new Buttonnew(this);
                buttonnew.setText(activity.get(i));
                layout_Activity.addView(buttonnew);
            }
    }

    public void dialog_Yes_No(Context context)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        MyDBHelperDiary dbHelperDiary = new MyDBHelperDiary(context,null,null,1);
                        dbHelperDiary.deleteActivity(diary_ID_Receiver);
                        dbHelperDiary.deleteDiaryNote(diary_ID_Receiver);
                        Intent intent = new Intent(context,MoodDiaryMainMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure about delete this diary note?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //kiểm tra xem đây là lệnh update hay lệnh insert
    private int check()
    {
        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag",0);
        diary_ID_Receiver = intent.getIntExtra("diary_id",0);
        return flag;
    }

    //set biểu cảm lấy từ intent add2
    private void setMood() {
        Intent intent = getIntent();
        String string = intent.getStringExtra("mood");
        str_Mood = string;
        ArrayList<String> arrayList = intent.getStringArrayListExtra("activity");
        activity = arrayList;


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
    // cài đặt đầu tiên cho màn hình khi được gọi để insert
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
        Log.d("magic!!!",currentDate.substring(0,2)+" "+currentDate.substring(3,5)+" "+currentDate.substring(6,10));
        bt_Date.setText(currentDate);
    }

    //xử lý kết quả trả về từ startActivityForResult
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
            options.inSampleSize=8;
            bitmap = BitmapFactory.decodeStream(in,null,options);
            imageView.setImageBitmap(bitmap);
        }
        if(requestCode == REQUEST_ACTIVITY && resultCode == RESULT_OK)
        {
            if (activity!=null)
                activity.clear();
            activity = data.getStringArrayListExtra("activity");
            layout_Activity.removeAllViews();
            if (activity != null)
                for (int i=0; i<activity.size(); i++)
                {
                    Buttonnew buttonnew = new Buttonnew(this);
                    buttonnew.setText(activity.get(i));
                    layout_Activity.addView(buttonnew);
                }
        }
    }

    //Insert diary note mới hoặc update diarynote đã có theo yêu cầu của intent
    public void insertOrUpdateDataToDiary()
    {
        MyDBHelperDiary helperDiary = new MyDBHelperDiary(this,null,null,1);
        DiaryNote diaryNote = new DiaryNote();
        diaryNote.setMoodID(helperDiary.getMoodIdByName(str_Mood));
        diaryNote.setDate(getTime());
        diaryNote.setHeadline(text_HeadLine.getText().toString());
        diaryNote.setNote(edt_Note.getText().toString());
        diaryNote.setPhoto(getBitmapAsByteArray(bitmap));
        if (check() ==0 || check() == 2)
            helperDiary.insertDiaryNote(diaryNote);
        else
            {
                diaryNote.setDiaryID(diary_ID_Receiver);
                helperDiary.deleteActivity(diary_ID_Receiver);
                helperDiary.updateDiary(diaryNote);
                insertDiary_Activity2();
            }

    }

    //gọi hàm thêm diary note activity với note hiện tại được chọn để update
    public void insertDiary_Activity2()
    {
        MyDBHelperDiary helperDiary = new MyDBHelperDiary(this,null,null,1);
        if (activity !=null)
            for (int i = 0; i < activity.size(); i++)
            {
                int activity_id = helperDiary.getActivityIdByActivityName(activity.get(i));
                helperDiary.insertDiary_Activity(diary_ID_Receiver, activity_id);
            }
    }

    // gọi hàm thêm diary note activity
    public void insertDiary_Activity()
    {
        MyDBHelperDiary helperDiary = new MyDBHelperDiary(this,null,null,1);
        int diary_id = helperDiary.getLastDiaryNoteID();
        if (activity != null)
        for (int i = 0; i < activity.size(); i++)
            {
                int activity_id = helperDiary.getActivityIdByActivityName(activity.get(i));
                helperDiary.insertDiary_Activity(diary_id, activity_id);
            }
    }

    //chuyển bitmap thành mảng byte để lưu trữ
    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) return  null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }



    ///yêu cầu cấp quyền hạn
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

    //sự kiện chọn thời gian bằng dialog
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
    //sự kiện chọn ngày bằng dialog
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
                dateSetListener, selectedYear, selectedMonth-1, selectedDayOfMonth);

// Show
        datePickerDialog.show();
        bt_Date.setText(selectedDayOfMonth+"/"+selectedMonth+"/" + selectedYear);
    }
    // chuyển đổi ngày sang millis giây
    private long getTime()
    {
        if (lastSelectedMinute == -1){
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }
        String date ="1-1-2000 0:0:0";
        date = selectedDayOfMonth + "-" + selectedMonth + "-" + selectedYear + " " + lastSelectedHour + ":" + lastSelectedMinute +":00";
        Log.d("magic!!!","string date: "+date);
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

    public void setAppLang(String local){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(local.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}
