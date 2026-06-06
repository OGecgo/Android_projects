package com.example.unipiaudiostories.Data.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.example.unipiaudiostories.Constants.Language;

import java.util.Locale;

public final class SettingsStorage {

    private static final String PREFS_NAME_APP_SETTINGS = "PREFS_NAME_APP_SETTINGS";
    private static final String KEY_LANGUAGE = "KEY_LANGUAGE";

    public static Context wrapContext(Context context) {
        String language = getLanguage(context);
        // Use a new blank Configuration to only override specific fields
        Configuration configuration = new Configuration();
        // locale
        Locale locale = Locale.forLanguageTag(language);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }


    public static void setLanguage(@NonNull Context context, @NonNull String language){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        pref.edit().putString(KEY_LANGUAGE, language).apply();
    }

    public static String getLanguage(@NonNull Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREFS_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
        return pref.getString(KEY_LANGUAGE, "en");
    }
}
