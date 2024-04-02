package com.andiradita.themovieapp.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val MOVIE_PREF = "movie_pref"
    private const val KEY_GENRE_LIST = "genre_list"

    fun saveGenreList(context: Context, data: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MOVIE_PREF, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_GENRE_LIST, data)
        editor.apply()
    }

    fun getGenreList(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MOVIE_PREF, Context.MODE_PRIVATE)
        val result = prefs.getString(KEY_GENRE_LIST, "")
        return result ?: ""
    }
}