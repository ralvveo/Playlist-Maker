package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.PlayerState

interface MediaplayerRepository {
    fun preparePlayer(previewUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()
    fun getState(): PlayerState
    fun setState(changedPlayerState: PlayerState)
}