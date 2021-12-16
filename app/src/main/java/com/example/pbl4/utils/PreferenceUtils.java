package com.example.pbl4.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public PreferenceUtils(){}
    public static boolean saveEmail(String email, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Constants.KEY_EMAIL,email);
        prefsEditor.apply();
        return true;
    }
    public static String getEmail(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_EMAIL,null);
    }
    public static boolean savePassword(String password, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD,password);
        prefsEditor.apply();
        return true;
    }
    public static String getPassword(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD,null);
    }
    public static boolean saveCheck(boolean check, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putBoolean(Constants.KEY_CHECK,check);
        prefsEditor.apply();
        return true;
    }
    public static boolean getCheck(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.KEY_CHECK,true);
    }
}
