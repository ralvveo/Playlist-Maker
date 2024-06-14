package com.practicum.playlistmaker.playlists.data.converters

import androidx.core.net.toUri
import com.practicum.playlistmaker.playlists.data.db.entity.PlaylistsEntity
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.model.PlaylistSerializable

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


    fun serializeMap(playlist: Playlist): PlaylistSerializable {
        return PlaylistSerializable(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescr = playlist.playlistDescr,
            playlistImage = playlist.playlistImage.toString(),
            playlistTrackIds = playlist.playlistTrackIds,
            playlistTrackCount = playlist.playlistTrackCount
        )
    }

    fun serializeMap(playlistSerializable: PlaylistSerializable): Playlist {
        return Playlist(
            playlistId = playlistSerializable.playlistId,
            playlistName = playlistSerializable.playlistName,
            playlistDescr = playlistSerializable.playlistDescr,
            playlistImage = playlistSerializable.playlistImage.toUri(),
            playlistTrackIds = playlistSerializable.playlistTrackIds,
            playlistTrackCount = playlistSerializable.playlistTrackCount
        )
    }
}