package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track

interface SearchHistoryFunctions {
    fun addTrackToHistory(track: Track)
    fun read(): MutableList<Track>
    fun write(trackList: MutableList<Track>)
    fun clear()
}