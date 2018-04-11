package com.example.srikanth.helloworld;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppDatabase dbInstance;

    // Static variables
    private static LocalTime sessionOne_start, sessionOne_deadline, sessionOne_end,
            sessionTwo_start, sessionTwo_deadline, sessionTwo_end,
            sessionThree_start, sessionThree_deadline, sessionThree_end;
    private static boolean sessionsSet = false;
    private static int myRollNumber = 20176001;

    // Listeners
    private final View.OnClickListener checkInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkIn(v);
        }
    },
    checkOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkOut(v);
        }
    },
    viewAttendanceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewAttendance(v);
        }
    };

    // Database ops
    private class ViewAttendanceTask extends AsyncTask<Void, Void, List<AttendanceEntry>> {

        @Override
        protected List<AttendanceEntry> doInBackground (Void...voids) {
            return dbInstance.getAttendanceEntryDao().getAll();
        }

        @Override
        protected void onPostExecute (List < AttendanceEntry > dataInDb) {
            if(dataInDb == null)
                System.out.println("null received from database");

            ArrayList<String> dateList = new ArrayList<>(), textList = new ArrayList<>();
            for (AttendanceEntry item : dataInDb) {
                dateList.add(item.getDate());
                textList.add("Sample text.");
            }

            Intent intent = new Intent(getApplicationContext(), ViewAttendanceActivity.class);
            Bundle bundle = new Bundle();
            intent.putStringArrayListExtra("dates", dateList);
            intent.putStringArrayListExtra("texts", textList);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        findViewById(R.id.checkInButton).setOnClickListener(checkInListener);
        findViewById(R.id.checkOutButton).setOnClickListener(checkOutListener);
        findViewById(R.id.viewAttendanceButton).setOnClickListener(viewAttendanceListener);

        dbInstance = AppDatabase.getInMemoryDatabase(getApplicationContext());

        if(!sessionsSet) {
            sessionOne_start = LocalTime.of(9, 0, 0);
            sessionOne_deadline = sessionOne_start.plusMinutes(10);
            sessionOne_end = LocalTime.of(11, 29, 59);

            sessionTwo_start = LocalTime.of(11, 30, 0);
            sessionTwo_deadline = sessionTwo_start.plusMinutes(10);
            sessionTwo_end = LocalTime.of(12, 59, 59);

            sessionThree_start = LocalTime.of(14, 0, 0);
            sessionThree_deadline = sessionThree_start.plusMinutes(10);
            sessionThree_end = LocalTime.of(18, 0, 0);

            sessionsSet = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("onCreateOptionsMenu called in MainActivity");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_link:
//                Toast.makeText(this, "You'll see your profile shortly...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ViewProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroy();
        super.onDestroy();
    }

//    private void populateDb() {
//        DatabaseInitializer.populateSync(dbInstance);
//    }

    // On-click methods.
    @TargetApi(Build.VERSION_CODES.O)
    public void checkIn(View view) {

        LocalTime currentTime = LocalTime.now();
        boolean inTimeForSessionOne = currentTime.compareTo(sessionOne_start) >= 0 && currentTime.compareTo(sessionOne_deadline) <= 0,
                inTimeForSessionTwo = currentTime.compareTo(sessionTwo_start) >= 0 && currentTime.compareTo(sessionTwo_deadline) <= 0,
                inTimeForSessionThree = currentTime.compareTo(sessionThree_start) >= 0 && currentTime.compareTo(sessionThree_deadline) <= 0;

        // If in time...
        if(inTimeForSessionOne || inTimeForSessionTwo || inTimeForSessionThree) {
            // Check if attendance for the day was already recorded...
            LocalTime startTime, deadlineTime;

            if(inTimeForSessionOne) {
                startTime = sessionOne_start;
                deadlineTime = sessionOne_deadline;
            } else if(inTimeForSessionTwo) {
                startTime = sessionTwo_start;
                deadlineTime = sessionTwo_deadline;
            } else {
                startTime = sessionThree_start;
                deadlineTime = sessionThree_deadline;
            }

            AttendanceEntry existingEntry = dbInstance.getAttendanceEntryDao().getAttendanceForOneSession(myRollNumber, (new Date()).toString(), startTime.toString(), deadlineTime.toString());
            if(existingEntry == null) {
                AttendanceEntry newAttendanceEntry = new AttendanceEntry(myRollNumber, new Date(), LocalTime.now());
                dbInstance.getAttendanceEntryDao().insert(newAttendanceEntry);
            } else {
                Toast.makeText(this, "You already checked in at " + existingEntry.getCheckInTime(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "This isn't a good time. Come back for one of the sessions.", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void checkOut(View view) {

        LocalTime currentTime = LocalTime.now();
        boolean inTimeForSessionOne = currentTime.compareTo(sessionOne_start) >= 0 && currentTime.compareTo(sessionOne_deadline) <= 0,
                inTimeForSessionTwo = currentTime.compareTo(sessionTwo_start) >= 0 && currentTime.compareTo(sessionTwo_deadline) <= 0,
                inTimeForSessionThree = currentTime.compareTo(sessionThree_start) >= 0 && currentTime.compareTo(sessionThree_deadline) <= 0;

        // If in time...
        if(inTimeForSessionOne || inTimeForSessionTwo || inTimeForSessionThree) {
            // Check if attendance for the day was already recorded...
            LocalTime startTime, deadlineTime, endTime;

            if(inTimeForSessionOne) {
                startTime = sessionOne_start;
                deadlineTime = sessionOne_deadline;
                endTime = sessionOne_end;
            } else if(inTimeForSessionTwo) {
                startTime = sessionTwo_start;
                deadlineTime = sessionTwo_deadline;
                endTime = sessionTwo_end;
            } else {
                startTime = sessionThree_start;
                deadlineTime = sessionThree_deadline;
                endTime = sessionThree_end;
            }

            // Check if the student has checked in...
            AttendanceEntry existingEntry = dbInstance.getAttendanceEntryDao().getAttendanceForOneSession(myRollNumber, (new Date()).toString(), startTime.toString(), deadlineTime.toString());
            if(existingEntry == null) {
                Toast.makeText(this, "You didn't check in for this session. Can't check out.", Toast.LENGTH_LONG).show();
            } else {
                AttendanceEntry newAttendanceEntry = new AttendanceEntry(myRollNumber, new Date(), LocalTime.parse(existingEntry.getCheckInTime()), LocalTime.now());
                dbInstance.getAttendanceEntryDao().update(newAttendanceEntry);
            }
        } else {
            Toast.makeText(this, "This isn't a good time. Come back for one of the sessions.", Toast.LENGTH_LONG).show();
        }
    }

    public void viewAttendance(View view) {
        ViewAttendanceTask viewAttendanceOp = new ViewAttendanceTask();
        viewAttendanceOp.execute();
    }
}

