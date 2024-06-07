package com.practicum.playlistmaker.playlists.data.converters

import androidx.core.net.toUri
import com.practicum.playlistmaker.playlists.data.db.entity.PlaylistsEntity
import com.practicum.playlistmaker.playlists.domain.model.Playlist

class PlaylistsDbConverter {
    fun map(playlist: Playlist): PlaylistsEntity {
        return PlaylistsEntity(
            playlistName = playlist.playlistName,
            playlistDescr = playlist.playlistDescr,
            playlistImage = playlist.playlistImage.toString(),
            playlistTrackIds = playlist.playlistTrackIds,
            playlistTrackCount = playlist.playlistTrackCount,
            playlistId = playlist.playlistId
        )
    }

    fun map(playlistsEntity: PlaylistsEntity): Playlist {
        return Playlist(
            playlistId = playlistsEntity.playlistId,
            playlistName = playlistsEntity.playlistName,
            playlistDescr = playlistsEntity.playlistDescr,
            playlistImage = playlistsEntity.playlistImage.toUri(),
            playlistTrackIds = playlistsEntity.playlistTrackIds,
            playlistTrackCount = playlistsEntity.playlistTrackCount

        )
    }
}