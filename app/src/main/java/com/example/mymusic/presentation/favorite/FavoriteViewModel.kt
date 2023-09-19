package com.example.mymusic.presentation.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    var favorite by mutableStateOf(Favorite(1, emptyList()))

    init {
        viewModelScope.launch {
            musicUseCases.getFavorite(1).collect{
                if(it.isNotEmpty())
                favorite = it[0]
            }
        }
    }

    fun upsertFavorite(song: Song){
        if(!favorite.songs.contains(song)){
            favorite.songs += song
            viewModelScope.launch {
                musicUseCases.insertFavorite(favorite)
            }
        }
    }

}