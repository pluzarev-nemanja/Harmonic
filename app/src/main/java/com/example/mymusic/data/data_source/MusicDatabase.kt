package com.example.mymusic.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymusic.domain.model.Song

@Database(
    entities = [Song::class],
    version = 1
)
abstract class MusicDatabase : RoomDatabase(){

    abstract val musicDao: MusicDao
}