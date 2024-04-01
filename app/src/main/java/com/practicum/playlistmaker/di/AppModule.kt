package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.player.ui.view_model.AudioplayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<MainViewModel>{
        MainViewModel()
    }

    viewModel<SettingsViewModel>(){
        SettingsViewModel()
    }

    viewModel<SearchViewModel>(){
        SearchViewModel()
    }

    viewModel<AudioplayerViewModel>(){(trackId: String) ->
        AudioplayerViewModel(trackId)
    }

}