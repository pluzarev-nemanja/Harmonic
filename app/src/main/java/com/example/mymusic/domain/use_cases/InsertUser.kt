package com.example.mymusic.domain.use_cases

import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.User
import com.example.mymusic.domain.repository.MusicRepository
import javax.inject.Inject

class InsertUser @Inject constructor(
    private val repository: MusicRepository,
) {

    suspend operator fun invoke(user: User) {
        repository.insertUser(user)
    }
}