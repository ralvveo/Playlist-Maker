package com.practicum.playlistmaker.player.domain.interactor

import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaplayerInteractor {

    fun preparePlayer(trackId: String)
    fun playbackControl()
    fun pausePlayer()
    fun release()

}