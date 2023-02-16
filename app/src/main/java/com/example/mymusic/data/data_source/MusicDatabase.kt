package com.example.mymusic.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymusic.domain.model.Song

@Database(
    entities = [Song::class],
    version = 1
)
@TypeConverters(UriConverters::class)
abstract class MusicDatabase : RoomDatabase(){

    abstract val musicDao: MusicDao
}