package com.infusiblecoder.allinonevideodownloader.extraFeatures.videolivewallpaper;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharedPrefUtils {
    private static final String PREF_APP = "pref_app_myprefs";

    private SharedPrefUtils() {
        throw new UnsupportedOperationException("Should not create instance of Util class. Please use as static..");
    }

    public static boolean getBooleanData(Context context, String str) {
        return context.getSharedPreferences(PREF_APP, 0).getBoolean(str, false);
    }

    public static int getIntData(Context context, String str) {
        return context.getSharedPreferences(PREF_APP, 0).getInt(str, 0);
    }

    public static String getStringData(Context context, String str) {
        return context.getSharedPreferences(PREF_APP, 0).getString(str, null);
    }

    public static void saveData(Context context, String str, String str2) {
        context.getSharedPreferences(PREF_APP, 0).edit().putString(str, str2).apply();
    }

    public static void saveData(Context context, String str, int i) {
        context.getSharedPreferences(PREF_APP, 0).edit().putInt(str, i).apply();
    }

    public static void saveData(Context context, String str, boolean z) {
        context.getSharedPreferences(PREF_APP, 0).edit().putBoolean(str, z).apply();
    }

    public static Editor getSharedPrefEditor(Context context, String str) {
        return context.getSharedPreferences(str, 0).edit();
    }

    public static void saveData(Editor editor) {
        editor.apply();
    }
}
