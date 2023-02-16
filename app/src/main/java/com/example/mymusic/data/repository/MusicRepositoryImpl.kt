package com.example.mymusic.data.repository

import com.example.mymusic.data.data_source.MusicDao
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val dao: MusicDao
) : MusicRepository {

    override suspend fun insertSongs(songList: MutableList<Song>) {
        dao.insertSongs(songList)
    }

    override fun getAllSongs(): Flow<List<Song>> {
        return dao.getAllSongs()
    }
}