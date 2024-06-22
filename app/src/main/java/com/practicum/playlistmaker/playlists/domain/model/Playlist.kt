package com.practicum.playlistmaker.playlists.domain.model

import android.net.Uri


data class Playlist (
    val playlistId: Long,
    var playlistName: String,
    var playlistDescr: String,
    var playlistImage: Uri,
    var playlistTrackIds: String,
    var playlistTrackCount: String
)
