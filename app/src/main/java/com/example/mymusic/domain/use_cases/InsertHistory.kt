package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class InsertHistory @Inject constructor(
    private val repository: MusicRepository,
) {

    suspend operator fun invoke(history: History) {
        repository.insertHistory(history)
    }
}