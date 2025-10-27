package com.example.expense_recording.Class;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.expense_recording.Enum.Date;


// use Shared Preferences for store data
public class SPDateManager implements com.example.expense_recording.Interfaces.DateManager{

    private static final String PREF_NAME = "DateManager";

    private Context context;

    public SPDateManager(Context context){ this.context = context; }
    public void setContext(Context context){ this.context = context;}
    public Context getContext(Context context) { return context; }

    public void update(int day, int month, int year){
        SharedPreferences prf = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prf.edit();
        edit.putInt("day", day);
        edit.putInt("month", month);
        edit.putInt("year", year);
        edit.apply();
    }

    public int read(Date d){
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
}
