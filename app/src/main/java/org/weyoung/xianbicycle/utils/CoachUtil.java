package org.weyoung.xianbicycle.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.weyoung.xianbicycle.BuildConfig;

public class CoachUtil {

    private static final String VERSION = "version";

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int version = sp.getInt(VERSION, 0);
        if (BuildConfig.VERSION_CODE != version) {
            sp.edit().putInt(VERSION, BuildConfig.VERSION_CODE).apply();
            return true;
        }
        return false;
    }
}
