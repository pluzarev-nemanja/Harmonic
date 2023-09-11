package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlbumsSongCount @Inject constructor(
    private val repository: MusicRepository,
) {

    operator fun invoke(): Flow<List<Album>> {
        return repository.getAllAlbumsSongCount()
    }
}