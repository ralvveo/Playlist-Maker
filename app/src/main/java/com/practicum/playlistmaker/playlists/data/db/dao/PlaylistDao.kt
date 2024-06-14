package com.practicum.playlistmaker.playlists.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.playlists.data.db.entity.PlaylistsEntity


@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlistsEntity: PlaylistsEntity)

    @Update(entity = PlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlistsEntity: PlaylistsEntity)


    @Query( "SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistsEntity>


}