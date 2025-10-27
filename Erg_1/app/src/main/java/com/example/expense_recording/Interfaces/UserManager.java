package com.example.expense_recording.Interfaces;

import android.content.Context;

import com.example.expense_recording.Enum.Date;
public interface UserManager {

    // download data from store
    void set_date_from_store();
    void change_date(int day, int month, int year);
    void add_record(int record);

    // methods for statistics
    int get_average(Date d);
    int get_max(Date d);
    int get_min(Date d);
    int total(Date d);

    int getDay();
    int getMonth();
    int getYear();
}
