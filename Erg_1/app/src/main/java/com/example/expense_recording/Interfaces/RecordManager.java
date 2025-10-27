package com.example.expense_recording.Interfaces;

import android.content.Context;

public interface RecordManager {

    // method update do insert too
    void update(int day, int month, int year, int record);
    int read(int day, int month, int year);
}
