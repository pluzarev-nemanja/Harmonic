package com.example.mymusic.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    var songs : List<Song>,
    @PrimaryKey(autoGenerate = true) val id : Int
)
