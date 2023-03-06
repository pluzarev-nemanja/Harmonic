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

    override fun getAllSongsAsc(): Flow<List<Song>> {
        return dao.getAllSongsAsc()
    }

    override fun getAllSongsDesc(): Flow<List<Song>> {
        return dao.getAllSongsDesc()
    }

    override fun getAllSongsArtist(): Flow<List<Song>> {
        return dao.getAllSongsArtist()
    }

    override fun getAllSongsAlbum(): Flow<List<Song>> {
        return dao.getAllSongsAlbum()
    }

    override fun getAllSongsDate(): Flow<List<Song>> {
        return dao.getAllSongsDate()
    }
}