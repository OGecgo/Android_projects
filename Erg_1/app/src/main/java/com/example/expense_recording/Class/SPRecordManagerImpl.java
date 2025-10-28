package com.example.expense_recording.Class;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.expense_recording.Interfaces.SharedPreferences.SPRecordManager;

// use Shared Preferences for store data
public class SPRecordManagerImpl implements SPRecordManager {
    private static final String PREF_NAME = "RecordManager";

    private Context context;

    public SPRecordManagerImpl(Context context){ this.context = context; }
    public void setContext(Context context){ this.context = context; }
    public Context getContext(){ return this.context; }

    public void update(int day, int month, int year, float record){
        SharedPreferences prf = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        float sum = record;
        sum += prf.getFloat(day + "/" + month + "/" + year, 0);
        SharedPreferences.Editor edit = prf.edit();
        edit.putFloat(day + "/" + month + "/" + year, sum);
        edit.apply();
    }
    public float read(int day, int month, int year){
        SharedPreferences prf = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prf.getFloat(day + "/" + month + "/" + year, 0);
    }

}
