package com.practicum.playlistmaker.main.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel() : ViewModel(), KoinComponent {

    private val themeSwitcherInteractor: ThemeSwitcherInteractor by inject()

    fun initialize() {
        themeSwitcherInteractor.setTheme()
    }

}