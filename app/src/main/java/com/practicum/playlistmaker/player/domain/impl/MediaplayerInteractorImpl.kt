package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.interactor.MediaplayerInteractor

class MediaplayerInteractorImpl(private val mediaplayer: MediaplayerRepositoryImpl) :
    MediaplayerInteractor {


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

