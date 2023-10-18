package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Artist(
    @PrimaryKey val artist : String,
    val numberSongs : Int,
    val numberAlbums: Int,
    var songs : List<Song>,
    )
