package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import com.practicum.playlistmaker.sharing.domain.ShareLinksOpenerInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent{

    private val shareLinksOpenerInteractor: ShareLinksOpenerInteractor by inject()
    private val themeSwitcherInteractor: ThemeSwitcherInteractor by inject()

    fun shareApp(){
        shareLinksOpenerInteractor.shareApp()
    }

    fun openTerms(){
        shareLinksOpenerInteractor.openTerms()
    }

    fun openSupport(){
        shareLinksOpenerInteractor.openSupport()
    }

    fun getTheme() : Boolean{
        return themeSwitcherInteractor.getTheme()
    }

    fun switchTheme(){
        themeSwitcherInteractor.switchTheme()
    }

}