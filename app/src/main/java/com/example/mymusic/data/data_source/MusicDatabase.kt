package com.example.mymusic.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.model.User

@Database(
    entities = [Song::class,Playlist::class,Album::class,History::class,Favorite::class,Artist::class,User::class],
    version = 1
)
@TypeConverters(UriConverters::class,DataConverters::class)
abstract class MusicDatabase : RoomDatabase(){

    abstract val musicDao: MusicDao
}