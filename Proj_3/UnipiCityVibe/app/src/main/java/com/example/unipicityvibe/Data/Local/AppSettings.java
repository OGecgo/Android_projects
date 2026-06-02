package com.example.unipicityvibe.Data.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.unipicityvibe.Enums.LocationTypeEnum;

public final class AppSettings {
    private static final String PREFS_NAME_APP_SETTINGS = "PREFS_NAME_APP_SETTINGS";

    private static final String KEY_FONT_SCALE = "KEY_FONT_SCALE";
    private static final String KEY_DARK_MODE = "KEY_DARK_MODE";
    private static final String KEY_LOCATION = "KEY_LOCATION";
    private static final String KEY_NOTIFICATION = "KEY_NOTIFICATION";


    // used only for font scale
    public static Context wrapContext(Context context) {
        float scale = getFontScale(context);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.fontScale = scale;
        return context.createConfigurationContext(config);
    }


    // ----- Font Scale -----
    public static void setFontScale(@NonNull Context context, float scale) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        prefs.edit().putFloat(KEY_FONT_SCALE, scale).apply();
    }
    public static float getFontScale(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_FONT_SCALE, 1.0f);
    }
    // ----- End Font Scale -----

    // ----- Dark Mode -----
    public static void applyDarkMode(boolean isDark) {
        AppCompatDelegate.setDefaultNightMode(isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
    public static void setDarkMode(@NonNull Context context, boolean isDark) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply();
        applyDarkMode(isDark);
    }
    public static boolean isDarkMode(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }
    // ----- End Dark Mode -----

    // ----- Location -----
    public static void setLocationAccuracy(@NonNull Context context, LocationTypeEnum e) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LOCATION, e.name()).apply();
    }
    public static LocationTypeEnum getLocationAccuracy(@NonNull Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_LOCATION, LocationTypeEnum.OFF_LOCATION.name());
        try {
            return LocationTypeEnum.valueOf(name);
        } catch (IllegalArgumentException e) {
            return LocationTypeEnum.OFF_LOCATION;
        }
    }
    // ----- End Location -----

    // ----- Notification -----
    public static void setNotificationPermission(@NonNull Context context, boolean permission){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_NOTIFICATION, permission).apply();
    }
    public static boolean getNotificationPermission(@NonNull Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_NOTIFICATION, false);
    }
    // ----- End Notification -----

}
