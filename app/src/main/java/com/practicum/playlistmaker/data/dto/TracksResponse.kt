package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.model.Track

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>)