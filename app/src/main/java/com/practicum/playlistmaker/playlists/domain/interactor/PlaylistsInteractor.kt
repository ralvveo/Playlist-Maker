package com.practicum.playlistmaker.playlists.domain.interactor

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {


    fun getPlaylistList(): Flow<List<Playlist>>
    fun getPlaylistTrackList(playlist: Playlist): Flow<List<String>>
    fun getAllTrackIds(): Flow<List<String>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean
    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Playlist
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun changePlaylist(playlist: Playlist)
}