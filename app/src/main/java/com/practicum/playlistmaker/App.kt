package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_INDICATOR = "dark_theme_indicator"
const val SEARCH_HISTORY = "search_history"


class App : Application() {

    private var darkTheme = false
    private var searchHistoryString: String? = ""

    companion object{
        var trackHistoryList = mutableListOf<Track>()
    }


    override fun onCreate(){
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
        searchHistoryString = sharedPrefs.getString(SEARCH_HISTORY, "")
        switchTheme(sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false))
        trackHistoryList = searchHistory.read()

    }



    fun switchTheme(darkThemeEnabled: Boolean)  {
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = darkThemeEnabled
        if (darkTheme) {
            sharedPrefs.edit()
                .putBoolean(DARK_THEME_INDICATOR, true)
                .apply()
        }
        else{
            sharedPrefs.edit()
                .putBoolean(DARK_THEME_INDICATOR, false)
                .apply()
        }

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )




    }

}