package com.example.mymusic.data.repository

import androidx.lifecycle.LiveData
import com.example.mymusic.data.data_source.MusicDao
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.repository.MusicRepository

class MusicRepositoryImpl(
    private val dao: MusicDao
) : MusicRepository {

    override suspend fun insertSongs(songList: ArrayList<Song>) {
        return dao.insertSongs(songList)
    }

    override fun getAllSongs(): LiveData<List<Song>> {
        return dao.getAllSongs()
    }
}