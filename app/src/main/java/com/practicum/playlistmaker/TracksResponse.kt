package com.practicum.playlistmaker

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>)