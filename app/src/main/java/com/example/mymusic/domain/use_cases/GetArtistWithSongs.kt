package com.example.mymusic.domain.use_cases

import com.example.mymusic.data.relations.AlbumWithSongs
import com.example.mymusic.data.relations.ArtistWithSongs
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtistWithSongs @Inject constructor(
    private val repository: MusicRepository,
) {

    operator fun invoke(artistName: String): Flow<List<ArtistWithSongs>> {
        return repository.getArtistWithSongs(artistName)
    }
}