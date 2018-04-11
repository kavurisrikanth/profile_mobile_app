package com.example.srikanth.helloworld;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {AttendanceEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE = null;

    public abstract AttendanceEntryDao getAttendanceEntryDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
            .build();
        }
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }
}