package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(
    @PrimaryKey val albumId : String,
    val albumName : String,
    val songs : List<Song>,
    val songCount: Int,
    val artist : String
)
