package com.example.mymusic.domain.repository

import com.example.mymusic.data.relations.AlbumWithSongs
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    suspend fun insertSongs(songList: MutableList<Song>)

    fun getAllSongsAsc(): Flow<List<Song>>

    fun getAllSongsDesc(): Flow<List<Song>>

    fun getAllSongsArtist(): Flow<List<Song>>

    fun getAllSongsAlbum(): Flow<List<Song>>

    fun getAllSongsDate(): Flow<List<Song>>

    fun searchBySongName(searchQuery: String): Flow<List<Song>>

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getAllPlaylistsDesc(): Flow<List<Playlist>>

    fun getAllPlaylistsSongCount(): Flow<List<Playlist>>

    fun getAllPlaylistsDuration(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun insertAlbums(albums: MutableList<Album>)

    suspend fun deleteAlbum(album: Album)

    fun getAllAlbums(): Flow<List<Album>>

    fun getAllAlbumsArtist(): Flow<List<Album>>

    fun getAllAlbumsSongCount(): Flow<List<Album>>

    fun getAlbumWithSongs(albumName: String): Flow<List<AlbumWithSongs>>


    suspend fun insertHistory(history: History)

    suspend fun insertFavorite(favorite: Favorite)

    fun getHistory(id: Int): Flow<List<History>>

    fun getFavorite(id: Int): Flow<List<Favorite>>

    suspend fun updateSong(song: Song)
}