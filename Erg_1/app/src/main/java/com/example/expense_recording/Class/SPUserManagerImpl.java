package com.example.expense_recording.Class;

import android.content.Context;

import com.example.expense_recording.Interfaces.RecordManager;
import com.example.expense_recording.Interfaces.DateManager;


import com.example.expense_recording.Enum.Date;

public class UserManagerImpl implements com.example.expense_recording.Interfaces.UserManager{
    private final RecordManager rec;
    private final DateManager date;
    private int day = -1;
    private int month = -1;
    private int year = -1;

    public UserManagerImpl (Context context){
        this.date = new SPDateManagerImpl(context);
        this.rec = new SPRecordManagerImpl(context);
        set_date_from_store();
    }
    public void update_context(Context context){
        this.date
    }

    public void set_date_from_store(){
        this.day = this.date.read(Date.DAY);
        this.month = this.date.read(Date.MONTH);
        this.year = this.date.read(Date.YEAR);
    }

    public void change_date(int day, int month, int year){
        this.date.update(day, month, year);
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public void add_record(Context context, int record){
        rec.update(context, this.day, this.month, this.year, record);
    }

    // methods for statistics
    public int get_average(Context context, Date d){
        int sum;
        switch (d){
            case WEEK:
                int day_week = this.day % 7;
                int day_start = this.day - day_week;

                sum = 0;
                for (int i = day_start; i <= day_start + 6; i ++){
                    sum += rec.read(context, i, this.month, this.year);
                }
                return (int)(sum/7);
            case MONTH:
                // if month % 2 == 0 then days = 30 else 31
                int days = 31 + (this.month % 2 - 1);
                if (this.month == 2) { // february
                    if (this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0)) {
                        days = 29;
                    } else {
                        days = 28;
                    }
                }

                sum = 0;
                for (int i = 1; i <= days; i ++){
                    sum += rec.read(context, i, this.month, this.year);
                }
                return (int)(sum/days);
            default:
                return -1;
        }
    }
    public int get_max(Date d){}
    public int get_min(Date d){}
    public int total(Date d){}
}
