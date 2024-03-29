package com.practicum.playlistmaker.settings.domain

interface ThemeSwitcherInteractor {

    fun switchTheme()
    fun setTheme()
    fun getTheme(): Boolean
}