package com.practicum.playlistmaker.playlists.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.playlists.domain.model.NewPlaylistState
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewPlaylistViewModel : ViewModel(), KoinComponent {


    private val state = MutableLiveData<NewPlaylistState>()
    private val newPlaylistsInteractor: PlaylistInteractor by inject()

    init {
        state.value = NewPlaylistState(null, null, null)
    }

    fun getState(): LiveData<NewPlaylistState> = state

    fun setName(name: String?) {
        state.value = state.value?.copy(newPlaylistName = name)
    }

    fun setDescr(descr: String?) {
        state.value = state.value?.copy(newPlaylistDescr = descr)
    }

    fun setImage(uri: Uri) {
        state.value = state.value?.copy(newPlaylistImage = uri)
    }

    fun createPlaylist() {
        viewModelScope.launch {
            newPlaylistsInteractor.createPlaylist(
                Playlist(
                    playlistId = 0,
                    playlistName = state.value?.newPlaylistName ?: "",
                    playlistDescr = state.value?.newPlaylistDescr ?: "",
                    playlistImage = state.value?.newPlaylistImage ?: Uri.EMPTY,
                    playlistTrackIds = "",
                    playlistTrackCount = "0"

                )
            )
        }

    }

}