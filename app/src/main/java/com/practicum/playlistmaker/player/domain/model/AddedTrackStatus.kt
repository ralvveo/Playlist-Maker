package com.practicum.playlistmaker.player.domain.model

import com.practicum.playlistmaker.playlists.domain.model.Playlist

data class AddedTrackStatus (
    val trackAdded: Boolean,
    val playlistName: String
)