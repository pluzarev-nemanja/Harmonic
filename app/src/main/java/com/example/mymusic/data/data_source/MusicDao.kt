package com.example.mymusic.data.data_source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymusic.domain.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songList: MutableList<Song>)

    @Query("SELECT * FROM song ORDER BY id ASC")
    fun getAllSongs(): Flow<List<Song>>
}