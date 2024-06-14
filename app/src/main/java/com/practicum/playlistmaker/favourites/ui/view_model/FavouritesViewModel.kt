package com.practicum.playlistmaker.favourites.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.domain.interactor.MediaInteractor
import com.practicum.playlistmaker.favourites.domain.state.MediaState
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class FavouritesViewModel : ViewModel(), KoinComponent {


    private val mediaLiveData = MutableLiveData<MutableList<Track>>()
    private val state = MutableLiveData<MediaState>()
    private val mediaInteractor: MediaInteractor by inject() {
        parametersOf(this)
    }

    fun getMediaLiveData(): LiveData<MutableList<Track>> = mediaLiveData
    fun getStateLiveData(): LiveData<MediaState> = state


    init{
        read()
    }

    fun checkFavouriteTracks(){
        read()
    }

    private fun processResult(trackList: List<Track>) {
        mediaLiveData.value = trackList.toMutableList()
        if (trackList.isNullOrEmpty()){
            state.postValue(MediaState.NoFavourite)
        }
        else {
            state.postValue(MediaState.MediaContent(trackList))
        }

    }

    private fun read() {
        viewModelScope.launch {
            mediaInteractor
                .getTrackList()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }






}