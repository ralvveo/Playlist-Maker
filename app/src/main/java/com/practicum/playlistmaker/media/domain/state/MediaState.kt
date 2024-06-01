package com.practicum.playlistmaker.media.domain.state

import com.practicum.playlistmaker.player.domain.model.Track

sealed interface MediaState {

    object NoFavourite : MediaState
    data class MediaContent(val mediaList: List<Track>) : MediaState
}