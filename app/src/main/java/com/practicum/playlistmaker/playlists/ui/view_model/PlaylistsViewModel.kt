package com.practicum.playlistmaker.playlists.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class PlaylistsViewModel : ViewModel(), KoinComponent {

    private val state = MutableLiveData<MutableList<Playlist>>()
    private val playlistsInteractor: PlaylistsInteractor by inject(){
        parametersOf(this)
    }

    init{
        state.value = mutableListOf()
        read()
    }

    fun getState(): LiveData<MutableList<Playlist>> = state


    fun read() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylistList()
                .collect { playlists ->
                    state.postValue(playlists.toMutableList())
                }
        }
    }
}