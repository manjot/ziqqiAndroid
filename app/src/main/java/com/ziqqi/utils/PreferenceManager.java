package com.ziqqi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static SharedPreferences preferences;

    private static SharedPreferences.Editor editor;

    private PreferenceManager() {

    }

    public static void init(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    public static void setStringValue(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean getBoolValue(String key) {
        return preferences.getBoolean(key, false);
    }

    public static void setBoolValue(String key, boolean value) {
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Integer getIntValue(String key) {
        return preferences.getInt(key, 0);
    }

    public static void setIntValue(String key, Integer value) {
        editor = preferences.edit();
        editor.putInt(key, value).apply();
    }

}
