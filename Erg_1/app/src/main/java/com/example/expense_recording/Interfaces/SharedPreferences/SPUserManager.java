package com.example.expense_recording.Interfaces.SharedPreferences;

import android.content.Context;

import com.example.expense_recording.Interfaces.UserManager;
public interface SPUserManager extends UserManager {
    void update_context(Context context);
}
