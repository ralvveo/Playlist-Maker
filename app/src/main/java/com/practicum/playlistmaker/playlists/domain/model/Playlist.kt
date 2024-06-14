package com.practicum.playlistmaker.playlists.domain.model

import android.net.Uri


data class Playlist (
    val playlistId: Long,
    val playlistName: String,
    val playlistDescr: String,
    val playlistImage: Uri,
    var playlistTrackIds: String,
    var playlistTrackCount: String
)
