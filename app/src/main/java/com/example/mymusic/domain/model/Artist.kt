package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Artist(
    @PrimaryKey val artist : String,
    val numberSongs : Int,
    val numberAlbums: Int,
    var songs : List<Song>,
    val artistImage : String
    ){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            artist
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
