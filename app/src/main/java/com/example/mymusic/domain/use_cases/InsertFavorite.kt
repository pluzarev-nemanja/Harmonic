package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class InsertFavorite @Inject constructor(
    private val repository: MusicRepository,
) {

    suspend operator fun invoke(favorite: Favorite) {
        repository.insertFavorite(favorite)
    }
}