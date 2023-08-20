package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class InsertPlaylist @Inject constructor(
    private val repository: MusicRepository,
    ) {

    suspend operator fun invoke(playlist: Playlist){
        repository.insertPlaylist(playlist)
    }
}