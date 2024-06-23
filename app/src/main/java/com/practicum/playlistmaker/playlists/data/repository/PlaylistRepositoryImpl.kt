package com.practicum.playlistmaker.playlists.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.practicum.playlistmaker.favourites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favourites.data.db.Database
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsDbConverter
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(private val context: Context, private val database: Database, private val converter: PlaylistsDbConverter, private val trackConverter: TrackDbConverter) : PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        database.playlistDao().createPlaylist(converter.map(playlist))
        //Saving playlist Image
        if (playlist.playlistImage != Uri.EMPTY)
            saveImage(playlist.playlistImage)
    }

    override fun getPlaylistList(): Flow<List<Playlist>> = flow{
        val playlistList = database.playlistDao().getPlaylists()
        emit(playlistList.map{ playlistEntity -> converter.map(playlistEntity) })
    }

    override fun getPlaylistTrackList(playlist: Playlist): Flow<List<String>> = flow{
        val playlistEntityList = database.playlistDao().getPlaylists()
        val playlistList = playlistEntityList.map{ playlistEntity -> converter.map(playlistEntity) }
        if (playlist !in playlistList) emit(listOf())
        else{
            val currentPlaylistIndex =  playlistList.indexOf(playlist)
            var trackIds = mutableListOf<String>()
            if (playlistList[currentPlaylistIndex].playlistTrackIds != "")
                trackIds = Json.decodeFromString<MutableList<String>>(playlistList[currentPlaylistIndex].playlistTrackIds)
            emit(trackIds)
        }
    }

    override fun getAllTrackIds(): Flow<List<String>> = flow{
        val playlistTrackIds = database.playlistDao().getAllTrackIds()
        val resultList = mutableListOf<String>()
        for (playlistTrackId in playlistTrackIds){
            var trackIds: MutableList<String>
            if (playlistTrackId.isNotEmpty()){
                trackIds = Json.decodeFromString<MutableList<String>>(playlistTrackId)
                for (trackId in trackIds){
                    if (trackId !in resultList)
                        resultList.add(trackId)
                }

            }
        }
        emit(resultList)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean {
        val getPlaylistList = database.playlistDao().getPlaylists()
        val playlistList = getPlaylistList.map{playlistsEntity ->  converter.map(playlistsEntity)}
        val currentPlaylistIndex =  playlistList.indexOf(playlist)
        val trackId = track.trackId
        var trackIds = mutableListOf<String>()
        if (playlistList[currentPlaylistIndex].playlistTrackIds != "")
            trackIds = Json.decodeFromString<MutableList<String>>(playlistList[currentPlaylistIndex].playlistTrackIds)
        if (trackId in trackIds)
            return false
        else {
            trackIds.add(element = trackId)
            val newTrackIds = Json.encodeToString(trackIds)
            playlistList[currentPlaylistIndex].playlistTrackIds = newTrackIds
            val trackCount = playlistList[currentPlaylistIndex].playlistTrackCount.toLong()
            playlistList[currentPlaylistIndex].playlistTrackCount = (trackCount + 1).toString()
            val newPlaylistEntity = converter.map(playlistList[currentPlaylistIndex])
            database.playlistDao().updatePlaylist(newPlaylistEntity)
            return true

        }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Playlist {
        val getPlaylistList = database.playlistDao().getPlaylists()
        val playlistList = getPlaylistList.map{playlistsEntity ->  converter.map(playlistsEntity)}
        val currentPlaylistIndex =  playlistList.indexOf(playlist)
        val trackId = track.trackId
        var trackIds = mutableListOf<String>()
        if (playlistList[currentPlaylistIndex].playlistTrackIds != "")
            trackIds = Json.decodeFromString<MutableList<String>>(playlistList[currentPlaylistIndex].playlistTrackIds)
        if (trackId in trackIds){
            trackIds.remove(trackId)
            val newTrackIds = Json.encodeToString(trackIds)
            playlistList[currentPlaylistIndex].playlistTrackIds = newTrackIds
            val trackCount = playlistList[currentPlaylistIndex].playlistTrackCount.toLong()
            playlistList[currentPlaylistIndex].playlistTrackCount = (trackCount - 1).toString()
            val newPlaylistEntity = converter.map(playlistList[currentPlaylistIndex])
            database.playlistDao().updatePlaylist(newPlaylistEntity)
        }
        checkTrackForDeleting(track)
        return playlistList[currentPlaylistIndex]


    }

    override suspend fun deletePlaylist(playlist: Playlist){
        database.playlistDao().deletePlaylist(converter.map(playlist))
    }

    override suspend fun changePlaylist(playlist: Playlist) {
        database.playlistDao().updatePlaylist(converter.map(playlist))
    }


    private fun saveImage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_ALBUM)
        val fileCodeName = uri.toString().takeLast(10)

        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$fileCodeName.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private suspend fun checkTrackForDeleting(track: Track){
        val trackIds = database.playlistDao().getAllTrackIds()
        var deleteTrack = true
        for (playlistTrackIds in trackIds)
            if (track.trackId in playlistTrackIds)
                deleteTrack = false
        if (deleteTrack)
            database.trackDao().deleteTrack(trackConverter.map(track))
    }

    companion object{
        const val MY_ALBUM = "myalbum"
    }
}