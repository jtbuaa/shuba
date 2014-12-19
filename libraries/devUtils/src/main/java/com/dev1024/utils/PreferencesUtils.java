package com.dev1024.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * PreferencesUtils
 * <p/>
 * Created by John on 2014-5-19.
 */
public class PreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


    public static void putInt(Context context, String name, String key, int value) {
        SharedPreferences pre = getSharedPreferences(context, name);
        Editor editor;
        editor = pre.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static void putString(Context context, String name, String key, String value) {
        if (value == null) return;
        if (context == null) return;
        SharedPreferences pre = getSharedPreferences(context, name);
        Editor editor;
        editor = pre.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String name, String key) {
        SharedPreferences pre = getSharedPreferences(context, name);
        return pre.getString(key, "");
    }

    public static int getInt(Context context, String name, String key) {
        SharedPreferences pre = getSharedPreferences(context, name);
        return pre.getInt(key, -1);
    }

    public static int getInt(Context context, String name, String key, int defaultValue) {
        SharedPreferences pre = getSharedPreferences(context, name);
        return pre.getInt(key, defaultValue);
    }

}
