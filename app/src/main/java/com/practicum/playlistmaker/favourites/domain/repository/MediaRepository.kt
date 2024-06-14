package com.practicum.playlistmaker.favourites.domain.repository

import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)

    fun getTrackList(): Flow<List<Track>>
    fun getTrackIds(): Flow<List<String>>
}