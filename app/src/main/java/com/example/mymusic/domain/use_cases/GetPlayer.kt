package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Player
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlayer @Inject constructor(
    private val repository: MusicRepository,
) {

    operator fun invoke(id: Int): Flow<List<Player>> {
        return repository.getPlayer(id)
    }

}
