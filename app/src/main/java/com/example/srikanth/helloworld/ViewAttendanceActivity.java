package com.example.srikanth.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {

    private static String[] dates, texts;
    private static boolean dataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> dateList = bundle.getStringArrayList("dates"),
                            textList = bundle.getStringArrayList("texts");

        if(!dataLoaded) {
            dates = dateList.toArray(dates);
            texts = textList.toArray(texts);

            AttendanceListAdapter adapter = new AttendanceListAdapter(this, dates, texts);
            ListView listView = findViewById(R.id.myAttendanceList);
            listView.setAdapter(adapter);

            dataLoaded = true;
        }
    }
}
