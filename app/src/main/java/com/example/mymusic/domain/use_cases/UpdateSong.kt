package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class UpdateSong @Inject constructor(
    private val repository: MusicRepository,
) {

    suspend operator fun invoke(song: Song) {
        repository.updateSong(song)
    }

}