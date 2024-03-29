package com.practicum.playlistmaker.player.domain.interactor

import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.MediaplayerInteractor
import com.practicum.playlistmaker.player.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.player.domain.repository.MyCallback

class MediaplayerInteractorImpl(private val mediaplayer: MediaplayerRepositoryImpl) : MediaplayerInteractor{


    override fun preparePlayer(trackId: String){
        mediaplayer.preparePlayer(trackId)
    }

    override fun playbackControl(){
        mediaplayer.playbackControl()
    }

    override fun pausePlayer(){
        mediaplayer.pausePlayer()
    }

    override fun release(){
        mediaplayer.release()
    }
}

