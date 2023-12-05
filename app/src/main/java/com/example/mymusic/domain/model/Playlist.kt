package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey val playlistName: String,
    val songCount: Int,
    val playlistDuration: Long,
    val songs: List<Song>,
    val playlistImage: String
)
