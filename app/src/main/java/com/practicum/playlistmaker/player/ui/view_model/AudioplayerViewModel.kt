package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.domain.interactor.MediaInteractor
import com.practicum.playlistmaker.player.domain.MediaplayerInteractor
import com.practicum.playlistmaker.player.domain.model.AddedTrackStatus
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class AudioplayerViewModel(private val track: Track) : ViewModel(), MyCallback, KoinComponent {

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private val trackIsFavouriteLiveData = MutableLiveData<Boolean>()
    private val playlistListLiveData = MutableLiveData<List<Playlist>>()
    private val trackAddedLiveData = MutableLiveData<AddedTrackStatus>()


    private val mediaplayerInteractor: MediaplayerInteractor by inject() {
        parametersOf(this)
    }
    private val playlistInteractor: PlaylistInteractor by inject() {
        parametersOf(this)
    }

    private val mediaInteractor: MediaInteractor by inject() {
        parametersOf(this)
    }

    fun getTrackAddedLiveData(): LiveData<AddedTrackStatus> = trackAddedLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    fun getTrackIsFavouriteLiveData(): LiveData<Boolean> = trackIsFavouriteLiveData
    fun getPlaylistListLiveData(): LiveData<List<Playlist>> = playlistListLiveData
    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = DEFAULT_TIME, isPlaying = false)
    }

    init {
        mediaplayerInteractor.preparePlayer(track.previewUrl)

    }

    fun checkFavourite() {
        viewModelScope.launch {
            mediaInteractor
                .getTrackList()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    fun checkPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylistList()
                .collect { playlistList ->
                    playlistListLiveData.postValue(playlistList)

                }
        }
    }

    private fun processResult(trackList: List<Track>) {
        trackIsFavouriteLiveData.value = track in trackList

    }

    fun insertTrack() {
        viewModelScope.launch { mediaInteractor.insertTrack(track) }
    }

    fun deleteTrack() {
        viewModelScope.launch { mediaInteractor.deleteTrack(track) }
    }

    fun playButtonClick() {
        mediaplayerInteractor.playbackControl()
    }

    fun pause() {
        mediaplayerInteractor.pausePlayer()
    }

    fun release() {
        mediaplayerInteractor.release()
    }

    override fun execute(message: String) {
        when (message) {
            "Play" -> playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
            "Pause" -> playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
            "TrackFinished" -> playStatusLiveData.value =
                getCurrentPlayStatus().copy(progress = DEFAULT_TIME, isPlaying = false)

            else -> playStatusLiveData.value = getCurrentPlayStatus().copy(progress = message)
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {

        viewModelScope.launch {
            trackAddedLiveData.postValue(
                AddedTrackStatus(
                    playlistInteractor.addTrackToPlaylist(track, playlist),
                    playlist.playlistName
                )
            )
            delay(100L)
            checkPlaylists()
        }

    }

    companion object {
        const val DEFAULT_TIME = "00:00"
    }
}