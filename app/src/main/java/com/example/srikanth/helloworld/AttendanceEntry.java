package com.example.srikanth.helloworld;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity(tableName = "attendance")
public class AttendanceEntry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    private int entryID;

    @NotNull
    @ColumnInfo(name="student_roll_number")
    private int rollNumber;

    @NotNull
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "check_in_time")
    private String checkInTime;

    @ColumnInfo(name = "check_out_time")
    private String checkOutTime;

    @ColumnInfo(name = "session")
    private int session;

//    public void checkIn(int rollNumber, Date date, LocalTime time) {
//        this.rollNumber = rollNumber;
//        this.date = date.toString();
//        this.checkInTime = time.toString();
//    }
//
//    public void checkOut(int rollNumber, Date date, LocalTime time) {
//        this.rollNumber = rollNumber;
//        this.date = date.toString();
//        this.checkOutTime = time.toString();
//    }

    public AttendanceEntry(int rollNumber, String date, String checkInTime, String checkOutTime, int session) {

    }

    public AttendanceEntry(int rollNumber, Date date, LocalTime checkInTime) {
        this.rollNumber = rollNumber;
        this.date = date.toString();
        this.checkInTime = checkInTime.toString();
        this.checkOutTime = null;
    }

    public AttendanceEntry(int rollNumber, Date date, LocalTime checkInTime, LocalTime checkOutTime) {
        this.rollNumber = rollNumber;
        this.date = date.toString();
        this.checkInTime = checkInTime.toString();
        this.checkOutTime = checkOutTime.toString();
    }

    public int getEntryID() {
        return entryID;
    }

    public void setEntryID(int entryID) {
        this.entryID = entryID;
    }

    @NotNull
    public int getRollNumber() {
        return rollNumber;
    }

    public int getSession() {
        return session;
    }

    public String getDate() {
        return date;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    //    public List<AttendanceEntry> getAll() {
//
//    }
}
