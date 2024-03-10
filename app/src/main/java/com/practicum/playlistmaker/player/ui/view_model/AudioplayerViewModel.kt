package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.repository.MyCallback

class AudioplayerViewModel(trackId: String): ViewModel(), MyCallback {

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    //private val mediaplayer = Creator.provideMediaplayer(callback = this)
    private val mediaplayerInteractor = Creator.provideMediaplayerInteractor(callback = this)
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = DEFAULT_TIME, isPlaying = false)
    }

    init{
        mediaplayerInteractor.preparePlayer(trackId)
    }

    fun playButtonClick(){
        mediaplayerInteractor.playbackControl()
    }

    fun pause(){
        mediaplayerInteractor.pausePlayer()
    }

    fun release(){
        mediaplayerInteractor.release()
    }
    override fun execute(message: String) {
        when (message){
            "Play" -> playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
            "Pause" -> playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
            "TrackFinished" -> playStatusLiveData.value = getCurrentPlayStatus().copy(progress = DEFAULT_TIME, isPlaying = false)
            else -> playStatusLiveData.value = getCurrentPlayStatus().copy(progress = message)
        }
    }


    companion object {
        fun factory(trackId: String): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AudioplayerViewModel(trackId)
                }
            }
        }
        const val DEFAULT_TIME = "00:00"
    }
}