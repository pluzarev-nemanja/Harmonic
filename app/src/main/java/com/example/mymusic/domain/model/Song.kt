package com.example.mymusic.domain.model

import android.net.Uri
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//album == albumName
@Entity(tableName = "song")
data class Song(
    val displayName:String,
    val artist:String,
    val data:String,
    val duration:Int,
    val title:String,
    val album: String,
    val uri: String,
    val artUri: String,
    val dateAdded: String,
    @PrimaryKey val id:Long
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            displayName
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
