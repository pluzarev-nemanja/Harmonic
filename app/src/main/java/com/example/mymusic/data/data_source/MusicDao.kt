package com.example.mymusic.data.data_source

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

    @Query("SELECT * FROM song ORDER BY displayName ASC")
    fun getAllSongsAsc(): Flow<List<Song>>

    @Query("SELECT * FROM song ORDER BY displayName DESC")
    fun getAllSongsDesc(): Flow<List<Song>>

    @Query("SELECT * FROM song ORDER BY artist ASC")
    fun getAllSongsArtist(): Flow<List<Song>>

    @Query("SELECT * FROM song ORDER BY album ASC")
    fun getAllSongsAlbum(): Flow<List<Song>>

    @Query("SELECT * FROM song ORDER BY dateAdded ASC")
    fun getAllSongsDate(): Flow<List<Song>>
}