package com.example.expense_recording.Class;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.expense_recording.Interfaces.SharedPreferences.SPDateManager;
import com.example.expense_recording.Interfaces.SharedPreferences.SPRecordManager;

import com.example.expense_recording.Enum.Date;
import com.example.expense_recording.Record.UserStatisticsData;

import com.example.expense_recording.Interfaces.SharedPreferences.SPUserManager;

public class SPUserManagerImpl implements SPUserManager{
    private final SPRecordManager rec;
    private final SPDateManager date;
    private final UserStatisticsData userStatWeek;
    private final UserStatisticsData userStatMonth;
    private int day = -1;
    private int month = -1;
    private int year = -1;

    private void set_stats(){
        if (this.day == -1 || this.month == -1 || this.year == -1) return;

        // MONTH STATS
        float total = 0;
        float max = 0;
        float min = Float.MAX_VALUE;

        // if month % 2 == 0 then days = 30 else 31

        int days = 31;
        if (this.month == 1 || this.month == 3 || this.month == 5 || this.month == 7 || this.month == 8 || this.month == 10 || this.month == 12) days = 30;
        if (this.month == 2) { // february
            if (this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0)) {
                days = 29;
            } else {
                days = 28;
            }
        }
        for (int i = 1; i <= days; i++){
            float temp = rec.read(i, this.month, this.year);
            total += temp;
            if (temp > max) max = temp;
            if (temp < min) min = temp;
        }

        this.userStatMonth.setAverage(total/days);
        this.userStatMonth.setMax(max);
        this.userStatMonth.setMin(min);
        this.userStatMonth.setTotal(total);

        // WEEK STATS
        total = 0;
        max = 0;
        min = Float.MAX_VALUE;

        int day_week = this.day % 7;
        int day_start = this.day - day_week;

        for (int i = day_start; i <= day_start + 6; i ++){
            float temp = rec.read(i, this.month, this.year);
            total += temp;
            if (temp > max) max = temp;
            if (temp < min) min = temp;
        }
        this.userStatWeek.setAverage(total/7);
        this.userStatWeek.setMax(max);
        this.userStatWeek.setMin(min);
        this.userStatWeek.setTotal(total);
    }

    public SPUserManagerImpl(Context context){
        this.date = new SPDateManagerImpl(context);
        this.rec = new SPRecordManagerImpl(context);
        this.userStatWeek  = new UserStatisticsData(-1, -1, -1, -1);
        this.userStatMonth = new UserStatisticsData(-1, -1, -1, -1);
        set_date_from_store();
    }
    public void update_context(Context context){
        this.rec.setContext(context);
        this.date.setContext(context);
    }

    public void set_date_from_store(){
        this.day   = this.date.read(Date.DAY);
        this.month = this.date.read(Date.MONTH);
        this.year  = this.date.read(Date.YEAR);
        set_stats();
    }

    public void change_date(int day, int month, int year){
        this.date.update(day, month, year);
        this.day = day;
        this.month = month;
        this.year = year;
        set_stats();
    }
    public void add_record(float record){
        rec.update(this.day, this.month, this.year, record);
        set_stats();
    }

    // methods for statistics
    public float get_average(@NonNull Date d){
        switch (d){
            case WEEK:
                return this.userStatWeek.getAverage();
            case MONTH:
                return this.userStatMonth.getAverage();
            default:
                return -1;
        }
    }
    public float get_max(@NonNull Date d){
        switch (d){
            case WEEK:
                return this.userStatWeek.getMax();
            case MONTH:
                return this.userStatMonth.getMax();
            default:
                return -1;
        }
    }
    public float get_min(@NonNull Date d){
        switch (d){
            case WEEK:
                return this.userStatWeek.getMin();
            case MONTH:
                return this.userStatMonth.getMin();
            default:
                return -1;
        }
    }
    public float total(@NonNull Date d){
        switch (d){
            case WEEK:
                return this.userStatWeek.getTotal();
            case MONTH:
                return this.userStatMonth.getTotal();
            default:
                return -1;
        }
    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }

}
