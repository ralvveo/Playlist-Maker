package com.practicum.playlistmaker.playlists.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlists_table")
data class PlaylistsEntity (
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    val playlistName: String,
    val playlistDescr: String,
    val playlistImage: String,
    val playlistTrackIds: String,
    val playlistTrackCount: String

)
