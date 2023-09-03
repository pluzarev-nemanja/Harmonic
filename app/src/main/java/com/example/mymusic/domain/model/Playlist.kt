package com.example.mymusic.domain.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey val playlistName: String,
    val songCount: Int,
    val playlistDuration: Long,
    val songs: List<Song>,
)
