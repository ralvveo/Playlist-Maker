package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.repository.ThemeSwitcherRepository

class ThemeSwitcherInteractorImpl(private val themeSwitcher: ThemeSwitcherRepository) : ThemeSwitcherInteractor{
    override fun switchTheme() {
        themeSwitcher.switchTheme()
    }

    override fun setTheme() {
        themeSwitcher.setTheme()
    }

    override fun getTheme(): Boolean {
        return themeSwitcher.getTheme()
    }
}