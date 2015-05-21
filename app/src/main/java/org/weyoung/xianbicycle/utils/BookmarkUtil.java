package org.weyoung.xianbicycle.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class BookmarkUtil {

    private static final String BOOKMARK = "bookmark";

    public static void save(Context context, String id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String raw = sp.getString(BOOKMARK, null);
        Type type = new TypeToken<LinkedHashSet<String>>() {}.getType();
        LinkedHashSet<String> ids = new Gson().fromJson(raw, type);
        if (ids == null) {
            ids = new LinkedHashSet<>();
        }
        boolean res = ids.add(id);
        if (res) {
            sp.edit().putString(BOOKMARK, new Gson().toJson(ids, type)).apply();
        }
    }

    public static void unsave(Context context, String id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String raw = sp.getString(BOOKMARK, null);
        Type type = new TypeToken<LinkedHashSet<String>>() {}.getType();
        LinkedHashSet<String> ids = new Gson().fromJson(raw, type);
        if (ids == null) {
            return;
        }
        boolean res = ids.remove(id);
        if (res){
            sp.edit().putString(BOOKMARK, new Gson().toJson(ids, type)).apply();
        }
    }

    public static List<String> getAll(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String raw = sp.getString(BOOKMARK, null);
        Type type = new TypeToken<LinkedHashSet<String>>() {}.getType();
        LinkedHashSet<String> ids = new Gson().fromJson(raw, type);
        if (ids == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(ids);
        }
    }

}
