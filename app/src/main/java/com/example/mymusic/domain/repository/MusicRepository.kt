package com.example.mymusic.domain.repository

import androidx.lifecycle.LiveData
import com.example.mymusic.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    suspend fun insertSongs(songList: MutableList<Song>)

    fun getAllSongs(): Flow<List<Song>>
}