package com.example.expense_recording.Interfaces;

import com.example.expense_recording.Enum.Date;

public interface DateManager {

    // method update do insert too
    void update(int day, int month, int year);
    int read(Date d);
}
