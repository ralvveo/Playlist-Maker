package com.practicum.playlistmaker.playlists.domain.model

import android.net.Uri

data class NewPlaylistState (
    val newPlaylistName: String?,
    val newPlaylistDescr: String?,
    val newPlaylistImage: Uri?
)




