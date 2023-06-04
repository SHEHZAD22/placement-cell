package com.shehzad.careerplacer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class MySharedPreferences {

    public static final String THEME_PREF = "themePref";

    private SharedPreferences preferences;

    public MySharedPreferences(Context context) {
        preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
    }

    public int getThemePref() {
        return preferences.getInt(THEME_PREF, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
    }

    public void setThemePref(int selectedTheme) {
        preferences.edit().putInt(THEME_PREF, selectedTheme).apply();
    }
}
