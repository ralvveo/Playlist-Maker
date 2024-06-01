package com.practicum.playlistmaker.settings.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import com.practicum.playlistmaker.sharing.domain.ShareLinksOpenerInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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