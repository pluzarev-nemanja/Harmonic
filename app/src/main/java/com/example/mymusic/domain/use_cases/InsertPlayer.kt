package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.Player
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class InsertPlayer @Inject constructor(
    private val repository: MusicRepository,
) {

    suspend operator fun invoke(player: Player) {
        repository.insertPlayer(player)
    }
}