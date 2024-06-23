package com.practicum.playlistmaker.playlists.domain.impl

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistRepository: PlaylistRepository) : PlaylistsInteractor{
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun changePlaylist(playlist: Playlist) {
        playlistRepository.changePlaylist(playlist)
    }

    override fun getPlaylistList(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistList()
    }

    override fun getPlaylistTrackList(playlist: Playlist): Flow<List<String>> {
        return playlistRepository.getPlaylistTrackList(playlist)
    }

    override fun getAllTrackIds(): Flow<List<String>> {
        return playlistRepository.getAllTrackIds()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Playlist {
        return playlistRepository.deleteTrackFromPlaylist(track, playlist)
    }
}