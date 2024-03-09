package com.practicum.playlistmaker.main.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class MainViewModel(context: Context) : ViewModel() {

    private val themeSwitcher = Creator.provideThemeSwitcher(context = context)

    init{
        themeSwitcher.setTheme()
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    MainViewModel(context)
                }
            }
        }
    }
}