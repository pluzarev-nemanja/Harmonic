package com.example.mymusic.domain.use_cases

import com.example.mymusic.data.relations.AlbumWithSongs
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumWithSongs @Inject constructor(
    private val repository: MusicRepository,
) {

     operator fun invoke(albumName: String): Flow<List<AlbumWithSongs>> {
        return repository.getAlbumWithSongs(albumName)
    }
}