package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.repository.ThemeSwitcherRepository

class ThemeSwitcherRepositoryImpl(val context: Context) : ThemeSwitcherRepository {

    override fun switchTheme()  {
        val sharedPrefs = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val currentTheme = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_INDICATOR, !currentTheme)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }
    override fun getTheme(): Boolean{
        val sharedPrefs = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        return sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
    }

    companion object{
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARK_THEME_INDICATOR = "dark_theme_indicator"
    }
}