package com.example.expense_recording.Interfaces.SharedPreferences;

import android.content.Context;

import com.example.expense_recording.Interfaces.RecordManager;
public interface SPRecordManager extends RecordManager {
    void setContext(Context context);
    Context getContext();
}
