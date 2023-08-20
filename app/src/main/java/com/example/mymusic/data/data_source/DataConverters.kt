package com.example.mymusic.data.data_source

import androidx.room.TypeConverter
import com.example.mymusic.domain.model.Song
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class DataConverters {

    @TypeConverter
    fun fromCountryLangList(value: List<Song>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCountryLangList(value: String): List<Song> {
        val gson = Gson()
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(value, type)
    }
}