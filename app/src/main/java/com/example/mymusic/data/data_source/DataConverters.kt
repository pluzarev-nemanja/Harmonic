package com.example.mymusic.data.data_source

import androidx.room.TypeConverter
import com.example.mymusic.domain.model.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverters {

    @TypeConverter
    fun toSongList(string: String): List<Song> {
        val listType = object : TypeToken<List<Song>>() {}.type
        return Gson().fromJson(string, listType) //here crash
    }

    @TypeConverter
    fun fromSongList(list: List<Song>): String {
        return Gson().toJson(list)
    }

}