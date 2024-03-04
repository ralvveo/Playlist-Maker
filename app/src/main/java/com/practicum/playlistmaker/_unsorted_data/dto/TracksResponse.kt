package com.practicum.playlistmaker._unsorted_data.dto

import com.practicum.playlistmaker._unsorted_domain.model.Track

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>)