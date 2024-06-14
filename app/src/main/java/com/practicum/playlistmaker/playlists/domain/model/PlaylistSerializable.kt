package com.practicum.playlistmaker.playlists.domain.model


import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSerializable (
    val playlistId: Long,
    val playlistName: String,
    val playlistDescr: String,
    val playlistImage: String,
    var playlistTrackIds: String,
    var playlistTrackCount: String
)