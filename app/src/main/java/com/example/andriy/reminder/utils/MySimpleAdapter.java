package com.example.andriy.reminder.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.andriy.reminder.R;

import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {

    public MySimpleAdapter(Context context,
                           List<? extends Map<String, ?>> data, int resource,
                           String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public void setViewText(TextView v, String text) {   // here we can change the color of text depending of value

        super.setViewText(v, text);

        if (v.getId() == R.id.tvReminderText) {
            if (text.matches("[-+]?\\d+")) {
                int i = Integer.parseInt(text);
                if (i < 80) v.setTextColor(Color.RED);
                else if (i > 80) v.setTextColor(Color.BLUE);
            } else v.setTextColor(Color.BLACK);
        }
    }
}
