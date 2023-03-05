package com.example.mymusic.domain.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "song")
data class Song(
    val uri: Uri,
    val displayName:String,
    @PrimaryKey val id:Long,
    val artist:String,
    val data:String,
    val duration:Int,
    val title:String,
    val album: String,
    val artUri: String,
    val dateAdded: String
)
