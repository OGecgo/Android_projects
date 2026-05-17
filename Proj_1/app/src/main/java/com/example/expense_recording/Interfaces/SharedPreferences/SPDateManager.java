package com.example.expense_recording.Interfaces.SharedPreferences;

import android.content.Context;

import com.example.expense_recording.Interfaces.DateManager;
public interface SPDateManager extends DateManager{
    void setContext(Context context);
    Context getContext();
}
