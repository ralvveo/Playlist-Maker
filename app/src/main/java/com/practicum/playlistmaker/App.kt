package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_INDICATOR = "dark_theme_indicator"
const val SEARCH_HISTORY = "search_history"


class App : Application() {

    private var darkTheme = false

    override fun onCreate(){
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
        switchTheme(sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean)  {
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = darkThemeEnabled
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_INDICATOR, darkTheme)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )




    }

}