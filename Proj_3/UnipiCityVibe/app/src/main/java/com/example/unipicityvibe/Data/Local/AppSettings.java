package com.example.unipicityvibe.Data.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

public final class AppSettings {
    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_FONT_SCALE = "font_scale";
    private static final String KEY_DARK_MODE = "dark_mode";

    public static void setFontScale(@NonNull Context context, float scale) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putFloat(KEY_FONT_SCALE, scale).apply();
    }

    public static float getFontScale(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_FONT_SCALE, 1.0f);
    }

    public static void setDarkMode(@NonNull Context context, boolean isDark) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply();
        applyDarkMode(isDark);
    }

    public static boolean isDarkMode(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public static void applyDarkMode(boolean isDark) {
        AppCompatDelegate.setDefaultNightMode(isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static Context wrapContext(Context context) {
        float scale = getFontScale(context);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.fontScale = scale;
        return context.createConfigurationContext(config);
    }
}
