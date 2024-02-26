package com.practicum.playlistmaker.presentation.use_case

import com.practicum.playlistmaker.ui.AudioplayerActivity

class ChangeCenterButtonToPlay {
    private val audioplayerActivity = AudioplayerActivity()
    fun execute(){
        audioplayerActivity.changeCenterButton("play")
    }
}