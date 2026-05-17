package com.example.expense_recording.Interfaces;

import com.example.expense_recording.Enum.Date;
public interface UserManager {

    // download data from store
    void set_date_from_store();
    // write date to store
    void change_date(int day, int month, int year);
    // write record to store
    void add_record(float record);

    // methods for statistics
    float get_average(Date d);
    float get_max(Date d);
    float get_min(Date d);
    float total(Date d);

    int getDay();
    int getMonth();
    int getYear();
}
