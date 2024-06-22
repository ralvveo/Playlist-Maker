package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favourites.ui.view_model.FavouritesViewModel
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.view_model.AudioplayerViewModel
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.root.ui.view_model.RootViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<RootViewModel>{
        RootViewModel()
    }

    viewModel<SettingsViewModel>(){
        SettingsViewModel()
    }

    viewModel<SearchViewModel>(){
        SearchViewModel()
    }

    viewModel<AudioplayerViewModel>(){(track: Track) ->
        AudioplayerViewModel(track)
    }

    viewModel<FavouritesViewModel>(){
        FavouritesViewModel()
    }

    viewModel<NewPlaylistViewModel>(){
        NewPlaylistViewModel()
    }

    viewModel<PlaylistsViewModel>(){
        PlaylistsViewModel()
    }

    viewModel<PlaylistViewModel>(){(playlist: Playlist) ->
        PlaylistViewModel(playlist)
    }

}