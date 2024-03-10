package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.domain.model.PlayStatus


interface MediaplayerRepository {
    fun preparePlayer(previewUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()
}