package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class UpdateAlbum @Inject constructor(
    private val repository: MusicRepository,
) {

    suspend operator fun invoke(album: Album) {
        repository.updateAlbum(album)
    }
}