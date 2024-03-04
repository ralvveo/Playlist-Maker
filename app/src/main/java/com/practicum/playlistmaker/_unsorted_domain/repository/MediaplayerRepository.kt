package com.practicum.playlistmaker._unsorted_domain.repository

import com.practicum.playlistmaker._unsorted_domain.model.PlayerState

interface MediaplayerRepository {
    fun preparePlayer(previewUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()
    fun getState(): PlayerState
    fun setState(changedPlayerState: PlayerState)
}