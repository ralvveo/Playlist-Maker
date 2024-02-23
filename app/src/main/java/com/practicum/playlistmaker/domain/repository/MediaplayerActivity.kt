package com.practicum.playlistmaker.domain.repository

interface MediaplayerActivity {
    fun preparePlayer(previewUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()
}