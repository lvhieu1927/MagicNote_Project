package com.example.magicnote1.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magicnote1.R;

import java.util.ArrayList;

public class todolist_mainMenu extends AppCompatActivity {
    private static final String TAG = "todolist_mainMenu";
    private dbTask dbTask;
    private ListView listViewTask;
    private ArrayAdapter<String> adapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_main_menu);

        dbTask = new dbTask(this);
        listViewTask = (ListView) findViewById(R.id.list_todo);
        updateListView();


    }
    public void handleClickButton(View v) {
        switch (v.getId()) {
            case R.id.add_item:
            final EditText editText = new EditText(this);
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("New Task")
                    .setMessage("Add new task here")
                    .setView(editText)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(editText.getText());
                            SQLiteDatabase db = dbTask.getWritableDatabase();
                            ContentValues value = new ContentValues();
                            value.put(TaskWork.taskForm.TASK_TITLE, task);
                            db.insertWithOnConflict(TaskWork.taskForm.TABLE, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                            updateListView();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            alertDialog.show();
        }
    }

    private void updateListView(){
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = dbTask.getReadableDatabase();
        Cursor cursor = db.query(TaskWork.taskForm.TABLE, new String[] {TaskWork.taskForm._ID, TaskWork.taskForm.TASK_TITLE}, null,null,null,null,null);
        while (cursor.moveToNext()){
            int i = cursor.getColumnIndex(TaskWork.taskForm.TASK_TITLE);
            list.add(cursor.getString(i));
        }
        if(adapter == null){
            adapter = new ArrayAdapter<>(this,R.layout.item_list,R.id.item_textView,list);
            listViewTask.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else{
            adapter.clear();
            adapter.addAll(list);

        }
        cursor.close();
        db.close();
    }
    public void doneTask(View v){
        View parent = (View) v.getParent();
        TextView textView = (TextView)parent.findViewById(R.id.item_textView);
        String task = String.valueOf(textView.getText());
        SQLiteDatabase db = dbTask.getWritableDatabase();
        db.delete(TaskWork.taskForm.TABLE,TaskWork.taskForm.TASK_TITLE + " = ?", new String[]{task});
        db.close();
        updateListView();
    }
//    public void SearchItem(View v){
//        items.add("4");
//        adapter.notifyDataSetChanged();
//        EditText test = (EditText)findViewById(R.id.search_task);
//        test.setText("");
//    }

}