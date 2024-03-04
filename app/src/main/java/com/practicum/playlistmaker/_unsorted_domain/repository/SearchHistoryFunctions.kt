package com.practicum.playlistmaker._unsorted_domain.repository

import com.practicum.playlistmaker._unsorted_domain.model.Track

interface SearchHistoryFunctions {
    fun addTrackToHistory(track: Track)
    fun read(): MutableList<Track>
    fun write(trackList: MutableList<Track>)
    fun clear()
}