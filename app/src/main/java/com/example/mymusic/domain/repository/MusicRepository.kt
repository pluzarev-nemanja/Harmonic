package com.example.mymusic.domain.repository

import androidx.lifecycle.LiveData
import com.example.mymusic.domain.model.Song

interface MusicRepository {

    suspend fun insertSongs(songList: ArrayList<Song>)

    fun getAllSongs(): LiveData<List<Song>>
}