package com.example.andriy.reminder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import com.example.andriy.reminder.DbOperations;
import com.example.andriy.reminder.utils.MySimpleAdapter;
import com.example.andriy.reminder.R;
import com.example.andriy.reminder.utils.DbCreator;

import java.util.ArrayList;
import java.util.Map;

import static com.example.andriy.reminder.utils.DBHelper.*;
import static com.example.andriy.reminder.R.layout.item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CM_DELETE_ID = 1;

    {
        new DbCreator().getDbInstance(this);
    }

    public static MySimpleAdapter sAdapter;
    public static ArrayList<Map<String, Object>> remindersList;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        new DbOperations().loadDataFromDbToList();
        buildTheListViewRemindersList();
    }

    public void buildTheListViewRemindersList() {

        ListView lvRemindersList = (ListView) findViewById(R.id.lvSimpleRemindersList);

        String[] from = {KEY_TEXT, KEY_TIME};   // the massive of reminderMap keys elements for ID Views and ListView filling
        int[] to = {R.id.tvReminderText, R.id.tvReminderTime};   // massive of ID Views

        sAdapter = new MySimpleAdapter(this, remindersList, item, from, to);
        lvRemindersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvRemindersList.setAdapter(sAdapter);                        // set the list to adapter
        registerForContextMenu(lvRemindersList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "delete selected item");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == CM_DELETE_ID) {   // get the items ID

            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            remindersList.remove(acmi.position);  // remove Map from List using the ID item
            String idSelect = String.valueOf(acmi.position + 1);

            sAdapter.notifyDataSetChanged();
            new DbOperations().deleteItem(idSelect);
            Toast.makeText(this, "deleted row id: " + idSelect, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }


    public void deleteItem(View view) {
        new DbOperations().deleteAllReminders();
        Toast.makeText(this, "remindersList clean", Toast.LENGTH_SHORT).show();
        remindersList.clear();
        sAdapter.notifyDataSetChanged();
    }

    public void onClick(View v) {                // open the Activity to set The Reminder
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }


}