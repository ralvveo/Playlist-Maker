package com.practicum.playlistmaker.presentation

import com.practicum.playlistmaker.domain.model.PlayerState
import com.practicum.playlistmaker.presentation.use_case.ChangeCenterButtonToPause
import com.practicum.playlistmaker.presentation.use_case.ChangeCenterButtonToPlay

class PlayerStateListenerImpl{

    val changeCenterButtonToPlayImpl = ChangeCenterButtonToPlay()
    val changeCenterButtonToPauseImpl = ChangeCenterButtonToPause()
    fun changeStateExecute(playerState: PlayerState){
        when (playerState){
            PlayerState.STATE_PLAYING -> changeCenterButtonToPauseImpl.execute()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> changeCenterButtonToPlayImpl.execute()
            else -> {}
        }
    }
}