package com.example.srikanth.helloworld;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface AttendanceEntryDao {

    @Insert(onConflict = IGNORE)
    void insert(AttendanceEntry entry);

    @Update(onConflict = IGNORE)
    void update(AttendanceEntry entry);

    @Query("SELECT * FROM attendance")
    List<AttendanceEntry> getAll();

    @Query("SELECT * FROM attendance WHERE " +
            "attendance.student_roll_number = :rollNumber " +
            "AND attendance.date = :date")
    List<AttendanceEntry> getAttendanceForADay(int rollNumber, String date);

    @Query("SELECT * FROM attendance WHERE " +
            "attendance.student_roll_number = :rollNumber " +
            "AND attendance.date = :date AND attendance.check_in_time >= :checkInStartTime AND " +
            "attendance.check_in_time <= :checkInDeadlineTime")
    AttendanceEntry getAttendanceForOneSession(int rollNumber, String date, String checkInStartTime, String checkInDeadlineTime);
}
