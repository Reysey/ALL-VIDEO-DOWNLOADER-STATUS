package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class Facebookprefloader {
    public static String DataFileName = "fb_prefvals";
    public static String fb_pref_cookie = "cookie";
    public static String fb_pref_key = "key";
    public static String fb_pref_isloggedin = "isloggedin";
    public static String PREFERENCE_itemFB = "fb_prefvals_item";

    public Context context;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;

    public Facebookprefloader(Context context) {
        this.context = context;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }


    public Map<String, String> LoadPrefString() {
        if (sharedPreference == null) {
            sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
        }
        try {
            Gson gson = new Gson();
            String storedHashMapString = sharedPreference.getString(PREFERENCE_itemFB, "oopsDintWork");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();

            return gson.<HashMap<String, String>>fromJson(storedHashMapString, type);
        } catch (Exception exception) {
            return null;
        }
    }

    public void SavePref(String str2, String str3) {
        if (editor == null) {
            sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
        }

        Map<String, String> map = new HashMap<>();

        map.put(fb_pref_cookie, str2);
        map.put(fb_pref_key, str2);
        map.put(fb_pref_isloggedin, str3);


        Gson gson = new Gson();
        String hashMapString = gson.toJson(map);

        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_itemFB, hashMapString);
        editor.apply();


    }

    public void MakePrefEmpty() {
        if (editor == null) {
            sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
        }

        Map<String, String> map = new HashMap<>();

        map.put(fb_pref_cookie, "");
        map.put(fb_pref_key, "");
        map.put(fb_pref_isloggedin, "false");

        Gson gson = new Gson();
        String hashMapString = gson.toJson(map);

        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_itemFB, hashMapString);
        editor.apply();
    }


    public Map<String, String> getPreference(String key) {
        try {
            Gson gson = new Gson();
            String storedHashMapString = sharedPreference.getString(PREFERENCE_itemFB, "oopsDintWork");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();

            return gson.<HashMap<String, String>>fromJson(storedHashMapString, type);
        } catch (Exception exception) {
            return null;
        }
    }


}
