package com.practicum.playlistmaker.playlists.domain.interactor

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    fun getPlaylistList(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean
}