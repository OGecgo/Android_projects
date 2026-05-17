package com.example.expense_recording.Interfaces;

public interface RecordManager {

    // method update do insert too
    void update(int day, int month, int year, float record);
    float read(int day, int month, int year);
}
