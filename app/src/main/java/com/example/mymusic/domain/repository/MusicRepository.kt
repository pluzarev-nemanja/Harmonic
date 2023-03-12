package com.example.mymusic.domain.repository

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
}