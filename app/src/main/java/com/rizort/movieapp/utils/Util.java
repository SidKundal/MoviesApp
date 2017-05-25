package com.rizort.movieapp.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rizort.movieapp.dtos.RecentSearchDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {

    public static void log(String whatToLog) {
//        Log.wtf(Constants.TAG, whatToLog);
    }

    public static boolean isNullOrBlank(String s) {
        return s == null || s.replace(" ", "").equals("");
    }

    /**
     * Retrieve the list of search suggestions
     *
     * @param context
     * @return
     */
    public static ArrayList<RecentSearchDTO> getListOfRecentSearches(Context context) {
        ArrayList<RecentSearchDTO> recentSearches = new ArrayList<>();
        String recentSearchesJSONString = SharedPrefUtils.getRecentSearchesJSONString(context);
        if(isNullOrBlank(recentSearchesJSONString)) {
            return recentSearches;
        } else {
            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<List<RecentSearchDTO>>() {
            }.getType();
            recentSearches = gson.fromJson(recentSearchesJSONString, listType);
            return recentSearches;
        }
    }

    /**
     * Add a search term entered by the user to the list of recent search suggestions
     *
     * NOTE: A previously saved suggestion is removed if the incoming suggestion has the same name
     *
     * @param context
     * @param searchSuggestion
     */
    public static void addToListOfRecentSearches(Context context, RecentSearchDTO searchSuggestion) {
        ArrayList<RecentSearchDTO> searchSuggestions = getListOfRecentSearches(context);
        Collections.reverse(searchSuggestions);
        int indexToRemove = -1;
        for(RecentSearchDTO sugg : searchSuggestions) {
            if(sugg.getSearchTerm().equals(searchSuggestion.getSearchTerm())) {
                indexToRemove = searchSuggestions.indexOf(sugg);
            }
        }
        if(indexToRemove != -1) {
            searchSuggestions.remove(indexToRemove);
        }
        if(searchSuggestions.size() == 2) {
            searchSuggestions.remove(0); // to make sure that we never have more than two saved recent searches
        }
        searchSuggestions.add(searchSuggestion);
        Collections.reverse(searchSuggestions);
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<RecentSearchDTO>>() {
        }.getType();
        String recentSearchesJSON = gson.toJson(searchSuggestions, listType);
        SharedPrefUtils.saveRecentSearchesJSONStringInPrefs(recentSearchesJSON, context);
    }
}
