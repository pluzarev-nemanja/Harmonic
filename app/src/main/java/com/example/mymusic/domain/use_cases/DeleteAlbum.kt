package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class DeleteAlbum @Inject constructor(
    private val repository: MusicRepository,
) {
    suspend operator fun invoke(album: Album){
        repository.deleteAlbum(album)
    }
}