package com.practicum.playlistmaker.playlists.domain.repository

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)

    fun getPlaylistList(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean
}