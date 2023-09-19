package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey val id: Int,
    var songs : List<Song>
)
