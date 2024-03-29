package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.player.domain.model.Track

interface SearchHistoryFunctionsRepository {
    fun read(): MutableList<Track>
    fun write(trackList: MutableList<Track>)
    fun clear()
}