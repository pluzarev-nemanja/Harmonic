package com.example.mymusic.presentation.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    var favorite by mutableStateOf(Favorite(1, emptyList()))
    var songList = mutableStateListOf<Song>()

    init {
        viewModelScope.launch {
            musicUseCases.getFavorite(1).collect {
                if (it.isNotEmpty()) {
                    favorite = it[0]
                    songList.clear()
                    songList += favorite.songs
                    songList.forEach { song ->
                        musicUseCases.updateSong(song.copy(isFavorite = true))
                    }
                }
            }
        }
    }

    fun upsertFavorite(song: Song) {

        viewModelScope.launch(Dispatchers.IO) {

            val newSong by mutableStateOf(song)
            newSong.isFavorite = !song.isFavorite

            musicUseCases.updateSong(newSong)

            songList.clear()
            songList += favorite.songs

            songList.remove(newSong)
            favorite.songs = songList

            if (newSong.isFavorite && !favorite.songs.contains(newSong)) {

                favorite.songs += newSong
                viewModelScope.launch {
                    musicUseCases.insertFavorite(favorite)
                }
            } else {
                songList.remove(newSong)
                favorite.songs = songList
                viewModelScope.launch {
                    musicUseCases.insertFavorite(favorite)
                }
            }
        }
    }
}