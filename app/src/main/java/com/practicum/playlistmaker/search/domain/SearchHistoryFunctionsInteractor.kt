package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.player.domain.model.Track

interface SearchHistoryFunctionsInteractor {
    fun read(): MutableList<Track>
    fun write(trackList: MutableList<Track>)
    fun clear()
}