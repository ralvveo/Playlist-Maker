package com.practicum.playlistmaker.settings.domain.repository

interface ThemeSwitcherRepository {
    fun switchTheme()
    fun getTheme(): Boolean
    fun setTheme()
}