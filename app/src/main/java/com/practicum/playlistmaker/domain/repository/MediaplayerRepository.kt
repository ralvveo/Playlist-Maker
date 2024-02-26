package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.PlayerState
import com.practicum.playlistmaker.presentation.PlayerStateListenerImpl

interface MediaplayerRepository {
    fun preparePlayer(previewUrl: String, listener: PlayerStateListenerImpl)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()

    fun getState(): PlayerState

    fun setState(changedPlayerState: PlayerState)
}