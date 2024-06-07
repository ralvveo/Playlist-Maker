package com.practicum.playlistmaker.playlists.domain.impl

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) : PlaylistInteractor{
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override fun getPlaylistList(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistList()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistRepository.addTrackToPlaylist(track, playlist)
    }
}