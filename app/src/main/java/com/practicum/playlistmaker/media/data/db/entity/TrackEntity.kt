package com.practicum.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavourite: Boolean
)

