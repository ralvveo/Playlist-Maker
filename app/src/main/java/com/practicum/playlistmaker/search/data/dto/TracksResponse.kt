package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.player.domain.model.Track

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>)