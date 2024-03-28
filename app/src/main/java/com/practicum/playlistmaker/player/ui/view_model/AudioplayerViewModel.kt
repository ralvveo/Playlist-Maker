package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.MediaplayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class AudioplayerViewModel(trackId: String): ViewModel(), MyCallback, KoinComponent {

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private val mediaplayerInteractor: MediaplayerInteractor by inject() {
        parametersOf(this)
    }
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
        const val DEFAULT_TIME = "00:00"
    }
}