package com.rizort.movieapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    public static final String SHARED_PREFS_FILE_NAME = "com.rizort.movieapp.sharedprefs";

    public static final String SHARED_PREFS_KEY_RECENT_SEARCHES     = "com.rizort.movieapp.sharedprefs.key.search.recentsearches";

    public static String getRecentSearchesJSONString(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SHARED_PREFS_KEY_RECENT_SEARCHES, null);
    }

    public static void saveRecentSearchesJSONStringInPrefs(String recentSearchesJSONString, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(SHARED_PREFS_KEY_RECENT_SEARCHES, recentSearchesJSONString).commit();
    }
}
