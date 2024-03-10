package com.practicum.playlistmaker.search.domain.model

import com.practicum.playlistmaker.player.domain.model.Track

interface RetrofitCallback {
    fun execute(trackList: MutableList<Track>)
}