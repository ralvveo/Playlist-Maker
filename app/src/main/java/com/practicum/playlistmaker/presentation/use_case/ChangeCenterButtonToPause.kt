package com.practicum.playlistmaker.presentation.use_case

import com.practicum.playlistmaker.ui.AudioplayerActivity

class ChangeCenterButtonToPause {
    private val audioplayerActivity = AudioplayerActivity()
    fun execute(){
        audioplayerActivity.changeCenterButton("pause")
    }
}