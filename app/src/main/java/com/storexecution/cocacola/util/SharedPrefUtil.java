package com.storexecution.cocacola.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    private static SharedPrefUtil sharePref = new SharedPrefUtil();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String PLACE_OBJ = "place_obj";

    private SharedPrefUtil() {
    } //prevent creating multiple instances by making the constructor private

    //The context passed into the getInstance should be application level context.
    public static SharedPrefUtil getInstance(Context context, int user_id) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName() , Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharePref;
    }
    public static SharedPrefUtil getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName() , Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharePref;
    }
    public void putString(String name, String content) {
        editor.putString(name, content)
                .apply();
    }

    public void putBoolean(String name, boolean content) {
        editor.putBoolean(name, content)
                .apply();
    }

    public void putInt(String name, int content) {
        editor.putInt(name, content)
                .apply();
    }
    public void putLong(String name, long content) {
        editor.putLong(name, content)
                .apply();
    }

    public String getString(String name, String defaultValue) {
        return sharedPreferences.getString(name, defaultValue);


    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return sharedPreferences.getBoolean(name, false);


    }

    public int getInt(String name, int defaultValue) {
        return sharedPreferences.getInt(name, defaultValue);


    }
    public long getLong(String name, long defaultValue) {
        return sharedPreferences.getLong(name, defaultValue);


    }

    public void remove(String name) {
        sharedPreferences.edit().remove(name)
                .apply();
    }
}
