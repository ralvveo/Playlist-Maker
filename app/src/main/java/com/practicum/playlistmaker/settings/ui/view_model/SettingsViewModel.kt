package com.practicum.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel(context: Context) : ViewModel(){

    private val shareLinksOpener = Creator.provideShareLinksOpener(context = context)
    private val themeSwitcher = Creator.provideThemeSwitcher(context = context)

    fun shareApp(){
        shareLinksOpener.shareApp()
    }

    fun openTerms(){
        shareLinksOpener.openTerms()
    }

    fun openSupport(){
        shareLinksOpener.openSupport()
    }

    fun getTheme() : Boolean{
        return themeSwitcher.getTheme()
    }

    fun switchTheme(){
        themeSwitcher.switchTheme()
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(context)
                }
            }
        }
    }
}