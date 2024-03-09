package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.player.domain.model.Track

interface RetrofitCallback {
    fun execute(trackList: MutableList<Track>)
}