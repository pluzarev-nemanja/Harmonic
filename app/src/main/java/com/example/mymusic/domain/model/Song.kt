package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class Song(
    val displayName: String,
    val artist: String,
    val data: String,
    val duration: Int,
    val title: String,
    val album: String,
    val uri: String,
    val artUri: String,
    val dateAdded: String,
    var isFavorite: Boolean,
    @PrimaryKey val id: Long
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            displayName
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
