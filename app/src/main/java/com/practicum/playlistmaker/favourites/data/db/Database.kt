package com.practicum.playlistmaker.favourites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favourites.data.db.dao.TrackDao
import com.practicum.playlistmaker.favourites.data.db.entity.TrackEntity
import com.practicum.playlistmaker.playlists.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.playlists.data.db.entity.PlaylistsEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistsEntity::class])
abstract class Database : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}