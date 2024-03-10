package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel : ViewModel(){

    private val shareLinksOpenerInteractor = Creator.provideShareLinksOpenerInteractor()
    private val themeSwitcherInteractor = Creator.provideThemeSwitcherInteractor()

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

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel()
                }
            }
        }
    }
}