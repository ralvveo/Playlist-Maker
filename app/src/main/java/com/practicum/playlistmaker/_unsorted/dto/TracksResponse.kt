package com.practicum.playlistmaker._unsorted.dto

import com.practicum.playlistmaker.player.domain.model.Track

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>)