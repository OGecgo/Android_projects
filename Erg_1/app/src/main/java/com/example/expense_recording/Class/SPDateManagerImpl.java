package com.example.expense_recording.Class;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.expense_recording.Enum.Date;

import com.example.expense_recording.Interfaces.SharedPreferences.SPDateManager;

// use Shared Preferences for store data
public class SPDateManagerImpl implements SPDateManager{

    private static final String PREF_NAME = "DateManager";

    private Context context;

    public SPDateManagerImpl(Context context){ this.context = context; }
    public void setContext(Context context){ this.context = context;}
    public Context getContext() { return context; }

    public void update(int day, int month, int year){
        SharedPreferences prf = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prf.edit();
        edit.putInt("day", day);
        edit.putInt("month", month);
        edit.putInt("year", year);
        edit.apply();
    }

    public int read(@NonNull Date d){
        SharedPreferences prf = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        switch (d){
            case DAY:
                return prf.getInt("day", -1);
            case MONTH:
                return prf.getInt("month", -1);
            case YEAR:
                return  prf.getInt("year", -1);
            default:
                return -1;
        }
    }

    // override object class

    @NonNull
    @Override
    public String toString(){
        int day = read(Date.DAY);
        int month = read(Date.MONTH);
        int year = read(Date.YEAR);
        return day + " / " + month + " / " + year;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof SPDateManager) {
            int day_o = ((SPDateManager) o).read(Date.DAY);
            int month_o = ((SPDateManager) o).read(Date.MONTH);
            int year_o = ((SPDateManager) o).read(Date.YEAR);

            int day = ((SPDateManager) o).read(Date.DAY);
            int month = ((SPDateManager) o).read(Date.MONTH);
            int year = ((SPDateManager) o).read(Date.YEAR);


            return day_o == day && month_o == month && year_o == year;
        }
        return false;
    }


}
