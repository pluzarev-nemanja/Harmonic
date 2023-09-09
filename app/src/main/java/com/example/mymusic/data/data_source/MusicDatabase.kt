package com.example.mymusic.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song

@Database(
    entities = [Song::class,Playlist::class,Album::class],
    version = 1
)
@TypeConverters(UriConverters::class,DataConverters::class)
abstract class MusicDatabase : RoomDatabase(){

    abstract val musicDao: MusicDao
}