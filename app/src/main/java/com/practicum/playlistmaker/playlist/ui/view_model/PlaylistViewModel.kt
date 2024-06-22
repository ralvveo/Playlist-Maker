package com.practicum.playlistmaker.playlist.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.domain.interactor.MediaInteractor
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class PlaylistViewModel(private var currentPlaylist: Playlist) : ViewModel(), KoinComponent {

    private val state = MutableLiveData<MutableList<Track>>()
    private val playlistsInteractor: PlaylistsInteractor by inject()
    private val mediaInteractor: MediaInteractor by inject() {
        parametersOf(this)
    }

    fun getState(): LiveData<MutableList<Track>> = state

    init {
        read()
    }

    fun read() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylistTrackList(currentPlaylist)
                .collect { trackIds ->
                    searchTrackInfo(trackIds)
                }
        }
    }

    private fun searchTrackInfo(trackIds: List<String>) {
        viewModelScope.launch {
            mediaInteractor
                .getTrackList()
                .collect { tracks ->
                    processResult(tracks, trackIds)
                }
        }
    }

    private fun processResult(trackList: List<Track>, trackIds: List<String>) {
        val resultTrackList = mutableListOf<Track>()
        for (trackId in trackIds) {
            for (track in trackList) {
                if (trackId == track.trackId)
                    resultTrackList.add(track)
            }
        }
        state.postValue(resultTrackList)

    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            currentPlaylist = playlistsInteractor.deleteTrackFromPlaylist(track, currentPlaylist)
            delay(200L)
            read()
        }


    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch{
            playlistsInteractor.deletePlaylist(playlist)
        }
    }

}