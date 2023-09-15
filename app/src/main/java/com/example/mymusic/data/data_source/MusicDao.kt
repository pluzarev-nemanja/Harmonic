package com.example.mymusic.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.mymusic.data.relations.AlbumWithSongs
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.History
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

    @Upsert
    suspend fun insertAlbums(albums: MutableList<Album>)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("SELECT * FROM album ORDER BY albumName ASC")
    fun getAllAlbums(): Flow<List<Album>>

    @Query("SELECT * FROM album ORDER BY artist ASC")
    fun getAllAlbumsArtist(): Flow<List<Album>>

    @Query("SELECT * FROM album ORDER BY songCount ASC")
    fun getAllAlbumsSongCount(): Flow<List<Album>>

    @Transaction
    @Query("SELECT * FROM album WHERE albumName = :albumName")
     fun getAlbumWithSongs(albumName : String) : Flow<List<AlbumWithSongs>>

    @Upsert
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getHistory(): Flow<History>

}