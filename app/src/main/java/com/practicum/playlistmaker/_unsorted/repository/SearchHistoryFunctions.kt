package com.practicum.playlistmaker._unsorted.repository

import com.practicum.playlistmaker.player.domain.model.Track

interface SearchHistoryFunctions {
    fun addTrackToHistory(track: Track)
    fun read(): MutableList<Track>
    fun write(trackList: MutableList<Track>)
    fun clear()
}