package com.example.mymusic.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.mymusic.domain.model.Playlist
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

    @Query("SELECT * FROM song ORDER BY dateAdded DESC")
    fun getAllSongsDate(): Flow<List<Song>>

    @Query("SELECT * FROM song WHERE displayName LIKE :searchQuery")
    fun searchBySongName(searchQuery: String): Flow<List<Song>>
    @Upsert
    suspend fun insertPlaylist(playlist: Playlist)
    @Delete
    suspend fun deletePlaylist(playlist: Playlist)
    @Query("SELECT * FROM playlist ORDER BY playlistName ASC")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY playlistName DESC")
    fun getAllPlaylistsDesc(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY songCount DESC")
    fun getAllPlaylistsSongCount(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY playlistDuration DESC")
    fun getAllPlaylistsDuration(): Flow<List<Playlist>>

    @Update
    suspend fun updatePlaylist(playlist: Playlist)
}