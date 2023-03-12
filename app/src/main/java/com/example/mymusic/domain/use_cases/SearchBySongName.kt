package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchBySongName @Inject constructor(
    private val repository: MusicRepository
) {

    operator fun invoke(searchQuery: String): Flow<List<Song>> {
        return repository.searchBySongName(searchQuery)
    }
}