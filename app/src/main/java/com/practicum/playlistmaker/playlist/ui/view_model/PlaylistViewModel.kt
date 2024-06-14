package com.practicum.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlaylistViewModel(private val currentPlaylist: Playlist) : ViewModel(), KoinComponent {

    private val state = MutableLiveData<MutableList<Track>>()
    private val playlistInteractor: PlaylistInteractor by inject()

    fun getState(): LiveData<MutableList<Track>> = state

    init{
        read()
    }
    fun read(){
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistTrackList(currentPlaylist)
                .collect { trackIds ->
                    searchTrackInfo(trackIds)
                }
        }
    }

    fun searchTrackInfo(trackIds: List<String>){

    }

}