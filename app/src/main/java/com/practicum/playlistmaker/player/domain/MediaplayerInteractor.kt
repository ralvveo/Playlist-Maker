package com.practicum.playlistmaker.player.domain

interface MediaplayerInteractor {

    fun preparePlayer(trackId: String)
    fun playbackControl()
    fun pausePlayer()
    fun release()
}