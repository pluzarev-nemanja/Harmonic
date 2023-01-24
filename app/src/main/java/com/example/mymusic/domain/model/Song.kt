package com.example.mymusic.domain.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "song")
data class Song(
    val url: String?,
    val displayName:String?,
    val columnId:Long?,
    val artist:String?,
    val data:String?,
    val duration:Int?,
    val title:String?
): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}
