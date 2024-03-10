package com.practicum.playlistmaker.main.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class MainViewModel() : ViewModel() {

    private val themeSwitcherInteractor = Creator.provideThemeSwitcherInteractor()

    init{
        themeSwitcherInteractor.setTheme()
    }

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    MainViewModel()
                }
            }
        }
    }
}