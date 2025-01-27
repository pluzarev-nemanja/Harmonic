package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistory @Inject constructor(
    private val repository: MusicRepository,
) {

     operator fun invoke(id: Int): Flow<List<History>>{
        return repository.getHistory(id)
    }

}